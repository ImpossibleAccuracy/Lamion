<template>
  <article>

    <form novalidate class="form md-layout" @submit.prevent="validate">
      <md-card class="form-content md-layout-item md-elevation-4 md-size-40 md-small-size-100">
        <md-card-header>
          <div class="md-title">Новое приложение</div>
        </md-card-header>

        <md-progress-bar md-mode="indeterminate" v-if="sending" />

        <md-card-content>

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

          <md-button type="submit"
            class="btn--submit md-raised md-primary"
            v-bind:disabled="sending">Создать</md-button>
        </md-card-content>
      </md-card>
    </form>

  </article>
</template>

<script>
import config from '@/config'
import api from '@/assets/scripts/api'
import { validationMixin } from 'vuelidate'
import { required, maxLength } from 'vuelidate/lib/validators'

export default {
  name: 'FormValidation',
  mixins: [validationMixin],
  data: () => ({
    form: {
      title: null
    },
    sending: false
  }),
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
  methods: {
    getValidationClass: function (fieldName) {
      const field = this.$v.form[fieldName]

      if (field) {
        return {
          'md-invalid': field.$invalid && field.$dirty
        }
      }
    },
    submit: async function () {
      this.sending = true

      try {
        const app = await api.apps.create(this.form)
        this.$router.push({
          name: 'ApplicationDetails',
          params: {
            id: app.id
          }
        })
      } catch (error) {
        console.error(error)
      }
    },
    validate: function () {
      this.$v.$touch()

      if (!this.$v.$invalid) {
        this.submit()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/form.scss';

.md-progress-bar {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
}

.md-card {
  .md-card-content {
    display: flex;
    flex-direction: column;
    align-items: center;

    .md-field.md-has-textarea {
      margin-top: 12px;
    }

    .btn--submit {
      width: auto;
    }
  }
}

</style>
