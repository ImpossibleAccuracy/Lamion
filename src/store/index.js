import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null,
    token: null,
    pageTitle: null
  },
  getters: {
    hasToken: (state) => {
      const token = state.token
      return (typeof token !== 'undefined' && token !== null)
    }
  },
  mutations: {
    setUser (state, user) {
      state.user = user
    },
    setToken (state, token) {
      state.token = token
    },
    setPageTitle (state, title) {
      state.pageTitle = title
    }
  },
  actions: {
  },
  modules: {
  }
})
