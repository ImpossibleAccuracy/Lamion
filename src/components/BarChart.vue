<template>
  <DoughnutChart
      v-bind:chart-data="chartData"
      v-bind:options="options" />
</template>

<script>
import Vue from 'vue'
import VueCompositionApi from '@vue/composition-api'
import { DoughnutChart } from 'vue-chart-3'
import { Chart, registerables } from 'chart.js'
import config from '@/config'

Vue.use(VueCompositionApi)
Chart.register(...registerables)

const getRandomColor = function () {
  const letters = '0123456789ABCDEF'.split('')
  const lettersCount = letters.length

  let color = '#'
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * lettersCount)]
  }

  return color
}

export default {
  name: 'BarChart',
  props: {
    data: Array
  },
  components: { DoughnutChart },
  data: function () {
    return {
      options: {
        responsive: true,
        maintainAspectRatio: true,
        onClick: this.onClick,
        plugins: {
          legend: {
            display: this.isLegendVisible()
          }
        }
      }
    }
  },
  methods: {
    isLegendVisible: function () {
      return this.data.length <= 15
    },
    onClick: function (point, evt) {
      if (evt.length > 0) {
        const index = evt[0].index
        const el = this.data[index]
        this.$emit('click', index, el)
      }
    }
  },
  computed: {
    chartData: function () {
      return {
        labels: this.data.map(i => {
          const label = i.label
          if (label.length > config.chart.label.maxLength) {
            return label.substring(0, config.chart.label.maxLength) + '...'
          } else {
            return label
          }
        }),
        datasets: [
          {
            data: this.data.map(i => i.value),
            backgroundColor: this.data.map(i => getRandomColor())
          }
        ]
      }
    }
  }
}
</script>
