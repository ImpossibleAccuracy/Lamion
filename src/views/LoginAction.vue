<template>
  <article>
    <form class="form md-layout" @submit.prevent="validate" novalidate>
      <md-card class="form-content md-layout-item md-elevation-4 md-size-30 md-small-size-100">
        <md-card-header>
          <div class="md-title">Войти в Lamion</div>
        </md-card-header>

        <md-card-content>
          <md-field :class="getValidationClass('email')">
            <label for="email">Email</label>
            <md-input id="email" type="email" name="email"
                autocomplete="email"
                v-model="form.email"
                v-on:focus="error.login = false"
                v-bind:disabled="UIDisabled" />

            <span class="md-error" v-if="!$v.form.email.required">Введите почту</span>
            <span class="md-error" v-else-if="!$v.form.email.email">Почта указана неверно</span>
            <span class="md-error" v-else-if="error.login">Пользователя с такими данными не существует</span>
          </md-field>

          <md-field :class="getValidationClass('password')">
            <label for="password">Пароль</label>
            <md-input id="password" type="password" name="password"
                autocomplete="password"
                v-model="form.password"
                v-on:focus="error.login = false"
                v-bind:disabled="UIDisabled"></md-input>

            <span class="md-error" v-if="!$v.form.password.required">Введите пароль</span>
            <span class="md-error" v-else-if="error.login">Пользователя с такими данными не существует</span>
          </md-field>

          <md-button type="submit"
            class="btn--submit md-raised md-primary"
            v-bind:disabled="UIDisabled">Войти</md-button>

          <div class="text-divider">
            <md-divider class="divider"></md-divider>
            <span class="text">ИЛИ</span>
          </div>

          <div class="action">
            <RouterLink to="/register" class="link md-body-1 md-primary" exact>Зарегистрироваться</RouterLink>
          </div>
        </md-card-content>

        <md-progress-bar md-mode="indeterminate" v-if="sending" />
      </md-card>

      <md-dialog-alert
        :md-active.sync="dialog.serverError"
        md-title="Ошибка!"
        md-content="Во время входа возникла ошибка. Повторите попытку позже." />
    </form>
  </article>
</template>

<script>
import { validationMixin } from 'vuelidate'
import { required, email } from 'vuelidate/lib/validators'
import api from '../assets/scripts/api'

export default {
  name: 'LoginAction',
  mixins: [validationMixin],
  data: () => ({
    form: {
      email: null,
      password: null
    },
    sending: false,
    dialog: {
      serverError: false
    },
    error: {
      login: false,
      server: false
    }
  }),
  computed: {
    UIDisabled: function () {
      return (this.sending || this.error.server)
    }
  },
  validations: {
    form: {
      email: {
        required,
        email
      },
      password: {
        required
      }
    }
  },
  methods: {
    getValidationClass: function (fieldName) {
      const field = this.$v.form[fieldName]

      if (field) {
        return {
          'md-invalid': ((field.$invalid && field.$dirty) || this.error.login)
        }
      }
    },
    validate: function () {
      this.$v.$touch()

      if (!this.$v.$invalid) {
        this.submit()
      }
    },
    submit: async function () {
      this.sending = true

      try {
        const json = await api.auth.login(this.form.email, this.form.password)
        this.onAuthSuccess(json)
      } catch (error) {
        this.onAuthFailed(error)
      }
    },
    onAuthSuccess: function (json) {
      this.$store.commit('setUser', {
        id: json.user.id,
        email: json.user.email
      })
      this.$store.commit('setToken', json.token)
      this.$router.replace('/')
    },
    onAuthFailed: function (err) {
      if (err.status === 400) {
        this.error.login = true
      } else {
        this.showDialog = true
        this.error.login = false
        this.error.server = true
        this.dialog.serverError = true
      }

      this.sending = false
    }
  }
}
</script>

<style lang="scss" scoped>
  @import '../assets/css/form.scss';
</style>
