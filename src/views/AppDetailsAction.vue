<template>
  <article>

    <md-card class="content" v-if="loaded">
      <md-card-header>
        <md-card-header-text class="md-toolbar-section-start">
          <md-button class="md-icon-button" v-on:click="$router.back()">
            <md-icon>arrow_back</md-icon>
          </md-button>
          <h3 class="md-title">{{ app.title }}</h3>
        </md-card-header-text>

        <md-menu md-size="medium" md-direction="bottom-end">
          <md-button class="md-icon-button" md-menu-trigger>
            <md-icon>more_vert</md-icon>
          </md-button>

          <md-menu-content>
            <md-menu-item v-on:click="dialogs.editApp = true">
              <span>Изменить</span>
              <md-icon>edit</md-icon>
            </md-menu-item>

            <md-menu-item v-on:click="dialogs.deleteApp = true">
              <span>Удалить</span>
              <md-icon>delete</md-icon>
            </md-menu-item>
          </md-menu-content>
        </md-menu>
      </md-card-header>

      <md-card-content>
        <p class="description">{{ app.description }}</p>

        <md-list>
          <md-list-item>Дата создания <code>{{ app.date }}</code></md-list-item>
          <md-list-item>Rоличество событий <code>{{ app.eventsCount }}</code></md-list-item>
          <md-list-item>Общее количество запросов <code>{{ app.requestsCount }}</code></md-list-item>
        </md-list>
      </md-card-content>

      <md-divider></md-divider>

      <md-card-content>
        <md-table md-sort="title"
            md-sort-order="asc"
            v-model="searched">

          <md-table-toolbar>
            <div class="md-toolbar-section-start">
              <h1 class="md-title">Список событий</h1>
            </div>

            <md-field md-clearable class="md-toolbar-section-end">
              <md-input placeholder="Поиск по названию" v-model="search" @input="searchOnTable" />
            </md-field>
          </md-table-toolbar>

          <md-table-row slot="md-table-row"
              slot-scope="{ item }"
              v-on:click="openEvent(item.id)">
            <md-table-cell md-label="Название" md-sort-by="title">{{ item.title }}</md-table-cell>
            <md-table-cell md-label="Количество запросов" md-sort-by="requestsCount">{{ item.requestsCount }}</md-table-cell>
          </md-table-row>

          <md-table-empty-state
            md-label="Ничего не найденно">
          </md-table-empty-state>
        </md-table>

      </md-card-content>

    </md-card>

    <md-card class="chart" v-if="loaded">
      <md-card-header>
        <div class="md-title">Диаграмма запросов по событиям</div>
      </md-card-header>

      <md-card-content>
        <BarChart v-bind:data="chartData" v-on:click="onChartClick" />
      </md-card-content>
    </md-card>

    <md-dialog-confirm
      md-title="Внимание"
      md-content="Вы не сможете восстановить приложение!"
      v-bind:md-active.sync="dialogs.deleteApp"
      v-on:md-confirm="deleteApp" />

    <md-dialog class="app-edit-dialog" v-bind:md-active.sync="dialogs.editApp">
      <!-- <md-dialog-title>Preferences</md-dialog-title> -->

      <md-dialog-content>

          <md-field v-bind:class="getValidationClass('title')">
            <label for="title">Название</label>
            <md-input id="title" type="text" name="title"
                md-counter="255"
                v-model="form.title"
                v-bind:disabled="sending" />

            <span class="md-error" v-if="!$v.form.title.required">Укажите название</span>
            <span class="md-error" v-else-if="!$v.form.title.maxLength">Название слишком длинное</span>
          </md-field>

          <md-field v-bind:class="getValidationClass('description')">
            <label for="description">Описание</label>
            <md-textarea id="description" name="description"
                md-counter="1024"
                v-model="form.description"
                v-bind:disabled="sending"></md-textarea>

            <span class="md-error" v-if="!$v.form.title.maxLength">Описание слишком длинное</span>
          </md-field>

      </md-dialog-content>

      <md-progress-bar md-mode="indeterminate" v-if="sending" />

      <md-dialog-actions>
        <md-button class="md-primary"
            v-bind:disabled="sending"
            v-on:click="dialogs.editApp = false">Закрыть</md-button>
        <md-button class="md-primary"
            v-bind:disabled="sending"
            v-on:click="validate">Сохранить</md-button>
      </md-dialog-actions>
    </md-dialog>

  </article>
</template>

<script>
import config from '@/config'
import api from '@/assets/scripts/api'
import utils from '@/assets/scripts/utils'
import BarChart from '@/components/BarChart'
import { validationMixin } from 'vuelidate'
import { required, maxLength } from 'vuelidate/lib/validators'

export default {
  name: 'AppDetailsAction',
  components: {
    BarChart
  },
  data: function () {
    return {
      app: {},
      events: [],
      searched: [],
      search: null,
      loaded: false,
      sending: false,
      form: {
        title: '',
        description: ''
      },
      dialogs: {
        editApp: false,
        deleteApp: false
      }
    }
  },
  mixins: [validationMixin],
  validations: {
    form: {
      title: {
        required,
        maxLength: maxLength(config.app.title.maxLength)
      },
      description: {
        maxLength: maxLength(config.app.description.maxLength)
      }
    }
  },
  computed: {
    chartData: function () {
      let array = this.searched
      if (array === null || array.length === 0) {
        array = this.events
      }

      return this.searched.map((e) => {
        return {
          label: e.title,
          value: e.requestsCount
        }
      })
    }
  },
  methods: {
    getValidationClass: function (fieldName) {
      const field = this.$v.form[fieldName]

      if (field) {
        return {
          'md-invalid': field.$invalid && field.$dirty
        }
      }
    },
    validate: function () {
      this.$v.$touch()

      if (!this.$v.$invalid) {
        this.editApp()
      }
    },
    editApp: async function () {
      this.sending = true
      try {
        this.app.title = this.form.title
        this.app.description = this.form.description

        await api.apps.update(this.app.id, {
          title: this.app.title,
          description: this.app.description
        })

        this.sending = false
        this.dialogs.editApp = false
      } catch (error) {
        console.error(error)
      }
    },
    deleteApp: async function () {
      try {
        await api.apps.delete(this.app.id)

        this.$router.back()
      } catch (error) {
        console.error(error)
      }
    },
    openEvent: function (id) {
      this.$router.push({
        name: 'EventsDetails',
        params: {
          app_id: this.app.id,
          id: id
        }
      })
    },
    searchOnTable: function () {
      this.searched = utils.searchByName(this.events, this.search)
    },
    onChartClick: function (index, item) {
      const event = this.searched[index]
      this.openEvent(event.id)
    }
  },
  mounted: async function () {
    try {
      const appId = Number(this.$route.params.id)

      this.app = await api.apps.get(appId)
      this.events = await api.events.load(appId)

      this.app.date = utils.buildDateString(new Date(this.app.date))

      this.form.title = this.app.title
      this.form.description = this.app.description

      this.searched = this.events
      this.$store.commit('setPageTitle', 'Приложение')

      this.loaded = true
    } catch (error) {
      if (error.status === 404) {
        this.$router.replace({
          name: 'PageNotFound'
        })
      } else {
        console.error(error)
      }
    }
  }
}
</script>

<style lang="scss">
@import '../assets/css/main';

.md-progress-bar {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
}

.article {
  .md-card {
    .md-card-content {
      .description {
        overflow: hidden;
        white-space: pre-wrap;
      }

      .md-list {
        .md-list-item-content {
          padding: 0;
        }
      }
    }
  }
}

.md-dialog.app-edit-dialog {
  .md-dialog-content {
    max-width: 100%;
    width: 768px;
  }
}

</style>
