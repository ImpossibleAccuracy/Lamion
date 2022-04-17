<template>
  <div id="app">

    <md-toolbar class="md-primary">
      <div class="md-toolbar-section-start">
        <md-button class="md-icon-button" v-on:click="showNavigation = true">
          <md-icon md-font-set="material-icons">menu</md-icon>
        </md-button>
        <h3 class="md-title">{{ title }}</h3>
      </div>
    </md-toolbar>

    <md-drawer :md-active.sync="showNavigation">

      <md-list v-if="user == null">
        <RouterLink to="/login" exact>
          <md-list-item href="#">
            <span class="md-list-item-text">Войти</span>
          </md-list-item>
        </RouterLink>

        <RouterLink to="/register" exact>
          <md-list-item href="#">
            <span class="md-list-item-text">Зарегистрироваться</span>
          </md-list-item>
        </RouterLink>

        <md-divider />

        <RouterLink to="/" exact>
          <md-list-item href="#">
            <span class="md-list-item-text">На главную</span>
          </md-list-item>
        </RouterLink>
      </md-list>

      <md-list v-else>

        <md-list-item>
          <span class="md-list-item-text">
            <span>{{ user.email }}</span>
            <span v-on:click="showLogoutDialog" class="btn-logout md-primary">Выйти</span>
          </span>
        </md-list-item>

        <md-divider />

        <RouterLink to="/" exact>
          <md-list-item href="#">
            <span class="md-list-item-text">На главную</span>
          </md-list-item>
        </RouterLink>

        <RouterLink to="/apps" exact>
          <md-list-item href="#">
            <span class="md-list-item-text">Приложения</span>
          </md-list-item>
        </RouterLink>

        <RouterLink to="/app/create" exact>
          <md-list-item href="#">
            <span class="md-list-item-text">Создать приложение</span>
          </md-list-item>
        </RouterLink>
      </md-list>

    </md-drawer>

    <router-view class="article" />

    <md-dialog-alert
      md-title="Error!"
      md-content="An error occurred during automatic login."
      v-bind:md-active.sync="dialogs.autoLoginFailed" />

    <md-dialog-confirm
      md-title="Выйти из Lamion?"
      md-confirm-text="Продолжить"
      md-cancel-text="Отмена"
      v-bind:md-active.sync="dialogs.logout"
      v-on:md-confirm="logout" />
  </div>

</template>

<script>
import Vue from 'vue'
import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import api from './assets/scripts/api'
import config from './config'

Vue.use(VueMaterial)

export default {
  name: 'App',
  data: function () {
    return {
      showNavigation: false,
      loginFailed: false,
      dialogs: {
        autoLoginFailed: false,
        logout: false
      }
    }
  },
  computed: {
    user () {
      return this.$store.state.user
    },
    token () {
      return this.$store.state.token
    },
    title () {
      return this.$store.state.pageTitle
    },
    isLoggedIn () {
      return (this.user != null)
    }
  },
  methods: {
    showLogoutDialog: function () {
      this.showNavigation = false
      this.dialogs.logout = true
    },
    login: async function () {
      try {
        const token = this.$store.state.token
        const json = await api.user.load(token)
        this.onAuthSuccess(json)
      } catch (error) {
        this.onAuthFailed(error)
      }
    },
    logout: function () {
      this.$store.commit('setUser', null)
      this.$store.commit('setToken', null)
      this.$router.go()
    },
    onAuthSuccess: function (json) {
      console.log(`Auto login: ${json.email}.`)
      this.$store.commit('setUser', {
        id: json.id,
        email: json.email
      })
    },
    onAuthFailed: function (error) {
      console.error(`Auto login failed: ${error}`)

      this.$store.commit('setUser', null)
      window.localStorage.clear()
      this.dialogs.autoLoginFailed = true
    }
  },
  created: function () {
    const token = localStorage.getItem('token')
    this.$store.commit('setToken', token)
    if (token !== null) {
      this.login()
    }
  },
  watch: {
    title: {
      immediate: true,
      handler (to, from) {
        document.title = to || config.applicationName
      }
    },
    token: {
      immediate: true,
      handler (to, from) {
        if (typeof from !== 'undefined' && to !== from) {
          if (to === null) {
            localStorage.clear()
          } else {
            localStorage.setItem('token', to)
          }
        }
      }
    }
  }
}
</script>

<style lang="scss">
@import './assets/css/app.scss';
@import 'https://fonts.googleapis.com/icon?family=Material+Icons';
$toolbar-height: 64px;

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  width: 100%;
  height: auto !important;
  min-height: 100vh !important;
}

body {
  font-family: Roboto, Avenir, Helvetica, Arial, sans-serif;
  background-color: $window-background;
  color: $on-window-background;
  overflow-x: hidden;
}

.md-drawer {
  .btn-logout {
    color: $primary;
    cursor: pointer;
    margin-top: 4px;
  }
}

.article {
  height: auto;
  min-height: calc(100% - $toolbar-height);
}

</style>
