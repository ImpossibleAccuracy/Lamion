<template>
  <article>

    <md-table
        class="content"
        md-card
        md-sort="id"
        md-sort-order="asc"
        v-model="searched"
        v-on:md-selected="onSelect">

      <md-table-toolbar>
        <div class="md-toolbar-row row-header">
          <div class="md-toolbar-section-start">
            <h1 class="md-title">Приложения</h1>
          </div>

          <md-field md-clearable class="md-toolbar-section-end">
            <md-input
                placeholder="Поиск по названию"
                v-bind:disabled="!hasApplications"
                v-model="search"
                v-on:input="searchOnTable" />
          </md-field>
        </div>
      </md-table-toolbar>

      <md-table-toolbar slot="md-table-alternate-header" slot-scope="{ count }">
        <div class="md-toolbar-section-start">{{ `${count} selected` }}</div>

        <div class="md-toolbar-section-end">
          <md-button class="md-icon-button" v-on:click="appsDeleteAttempt = true">
            <md-icon>delete</md-icon>
          </md-button>
        </div>
      </md-table-toolbar>

      <md-table-empty-state
        v-if="loaded"
        md-label="Ничего не найденно"
        v-bind:md-description="emptyStateDescription">

        <RouterLink to="/app/create" exact>
          <md-button class="md-primary md-raised">Создать приложение</md-button>
        </RouterLink>
      </md-table-empty-state>

      <md-table-row slot="md-table-row"
          slot-scope="{ item }"
          md-selectable="multiple"
          v-on:click="openDetails(item.id)">

        <md-table-cell md-label="ID" md-sort-by="id" md-numeric>{{ item.id }}</md-table-cell>
        <md-table-cell md-label="Название" md-sort-by="title" class="title">{{ item.title }}</md-table-cell>
        <md-table-cell md-label="Дата создания" md-sort-by="date">{{ item.date }}</md-table-cell>
        <md-table-cell md-label="Количество событий" md-sort-by="eventsCount">{{ item.eventsCount }}</md-table-cell>
        <md-table-cell md-label="Количество запросов" md-sort-by="requestsCount">{{ item.requestsCount }}</md-table-cell>
      </md-table-row>
    </md-table>

    <md-card class="chart">
      <md-card-header>
        <div class="md-title">Диаграмма событий по приложениям</div>
      </md-card-header>

      <md-card-content>
        <BarChart v-bind:data="chartData" v-on:click="onChartClick" />
      </md-card-content>
    </md-card>

    <md-dialog-confirm
      md-title="Внимание"
      md-content="Вы не сможете восстановить приложения!"
      v-bind:md-active.sync="appsDeleteAttempt"
      v-on:md-confirm="deleteSelected" />
  </article>
</template>

<script>
import api from '@/assets/scripts/api'
import utils from '@/assets/scripts/utils'
import BarChart from '@/components/BarChart'

export default {
  name: 'TableSearch',
  components: {
    BarChart
  },
  data: () => ({
    loaded: false,
    applications: [],
    selected: [],
    searched: [],
    search: null,
    selectAll: false,
    appsDeleteAttempt: false
  }),
  computed: {
    hasApplications: function () {
      return (this.applications.length !== 0)
    },
    emptyStateDescription: function () {
      if (this.hasApplications) {
        return `Ничего не найденно по запросу "${this.search}". Попробуйте перефразировать свой запрос.`
      } else {
        return 'Создайте приложение, и оно появится здесь.'
      }
    },
    chartData: function () {
      return this.searched.map((a) => {
        return {
          label: a.title,
          value: a.eventsCount
        }
      })
    }
  },
  methods: {
    searchOnTable () {
      this.searched = utils.searchByName(this.applications, this.search)
    },
    onSelect (items) {
      this.selected = items
    },
    openDetails (appId) {
      this.$router.push({
        name: 'ApplicationDetails',
        params: {
          id: appId
        }
      })
    },
    onChartClick: function (index, item) {
      const app = this.searched[index]
      this.openDetails(app.id)
    },
    deleteSelected: async function () {
      for (const app of this.selected) {
        await api.apps.delete(app.id)
      }

      this.applications = this.applications.filter(a => {
        for (const s of this.selected) {
          if (a.id === s.id) return false
        }
        return true
      })

      this.searchOnTable()
    }
  },
  mounted: async function () {
    try {
      this.applications = await api.apps.load()

      for (const app of this.applications) {
        app.date = utils.buildDateString(new Date(app.date))
      }

      this.searched = this.applications
      this.loaded = true
    } catch (error) {
      console.error(error)
    }
  }
}
</script>

<style lang="scss">
@import '../assets/css/main';

.md-table {
  .md-toolbar-row.row-header {
    @media screen and (max-width: 618px) {
      .md-title {
        margin: 0;
      }

      & {
        display: block;
        margin-top: 8px;
      }
    }
  }

  .md-table-content {
    max-height: unset !important;
    height: auto !important;
  }

  .md-table-row:not(.md-selected).md-checkbox,
  .md-checkbox:not(.md-checked) {
    opacity: .3;
  }
}

</style>
