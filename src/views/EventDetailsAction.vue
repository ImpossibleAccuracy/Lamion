<template>
  <article>

    <md-card class="content" v-if="loaded">
      <md-card-header>
        <div class="md-toolbar-section-start">
          <md-button class="md-icon-button" v-on:click="$router.back()">
            <md-icon>arrow_back</md-icon>
          </md-button>
          <h3 class="md-title">{{ event.title }}</h3>
        </div>
      </md-card-header>

      <md-card-content>
        <md-list>
          <md-list-item>Дата создания <code>{{ event.date }}</code></md-list-item>
          <md-list-item>Общее количество запросов <code>{{ event.requestsCount }}</code></md-list-item>
          <md-list-item>Устройство с наибольшим количеством вызовов <code>{{ event.mostUsedDevice }}</code></md-list-item>
          <md-list-item>Среднее время активности <code>{{ event.highDemandTime }}</code></md-list-item>
        </md-list>
      </md-card-content>

      <md-card-expand>
        <md-card-actions>
          <md-card-expand-trigger>
            <md-button v-on:click.once="onExpand">Просмотреть все запросы</md-button>
          </md-card-expand-trigger>
        </md-card-actions>

        <md-card-expand-content>
          <md-card-content>
            <md-divider></md-divider>

            <md-table v-if="requestsLoaded"
                md-sort="date"
                md-sort-order="desc"
                v-model="searched">

              <md-table-toolbar>
                <div class="md-toolbar-section-start">
                  <h1 class="md-title">Список запросов</h1>
                </div>
              </md-table-toolbar>

              <md-table-row slot="md-table-row"
                  slot-scope="{ item }">
                  <md-table-cell md-label="Устройство" md-sort-by="device">{{ item.device }}</md-table-cell>
                  <md-table-cell md-label="Время" md-sort-by="date">{{ item.date }}</md-table-cell>
              </md-table-row>

              <md-table-empty-state
                  md-label="Ничего не найденно">
              </md-table-empty-state>
            </md-table>
          </md-card-content>
        </md-card-expand-content>
      </md-card-expand>

    </md-card>

    <md-card class="chart" v-if="loaded">
      <md-card-header>
        <div class="md-title">Диаграмма запросов по событиям</div>
      </md-card-header>

      <md-card-content>
        <BarChart v-bind:data="chartData" />
      </md-card-content>
    </md-card>

  </article>
</template>

<script>
import utils from '@/assets/scripts/utils'
import BarChart from '../components/BarChart'
import api from '@/assets/scripts/api'
import config from '@/config'

export default {
  name: 'AppDetailsAction',
  components: {
    BarChart
  },
  data: function () {
    return {
      event: null,
      requests: null,
      requestsAnalytics: null,
      searched: null,
      search: null,
      loaded: false,
      requestsLoaded: false
    }
  },
  computed: {
    chartData: function () {
      return this.requestsAnalytics.map(r => {
        return {
          label: r.device || config.request.title.default,
          value: r.count
        }
      })
    }
  },
  methods: {
    searchOnTable: function () {
      this.searched = utils.searchByName(this.events, this.search)
    },
    onChartClick: function (index, item) {
      const event = this.searched[index]
      this.openEvent(event.id)
    },
    onExpand: async function () {
      if (this.requestsLoaded) return

      const appId = Number(this.$route.params.app_id)
      const id = Number(this.$route.params.id)

      this.requests = await api.requests.load(appId, id)

      for (const request of this.requests) {
        if (!request.device) {
          request.device = config.request.title.default
        }
        request.date = utils.buildDateString(new Date(request.date))
      }

      this.searched = this.requests
      this.requestsLoaded = true
    }
  },
  mounted: async function () {
    try {
      const appId = Number(this.$route.params.app_id)
      const id = Number(this.$route.params.id)

      this.event = await api.events.get(appId, id)
      this.requestsAnalytics = await api.requests.loadAnalytics(appId, id)

      this.event.date = utils.buildDateString(new Date(this.event.date))

      this.$store.commit('setPageTitle', 'Событие')
      this.searched = this.requests

      this.loaded = true
      this.requestsLoaded = false
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
</style>
