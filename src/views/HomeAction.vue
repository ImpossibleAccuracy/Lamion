<template>
  <article>
    <div class="logged" v-if="isLoggedIn && loaded">
      <h1 class="md-display-2">Кол-во ваших приложений: </h1>
      <RouterLink to="/apps" class="md-display-2" exact>{{ appsCount }}</RouterLink>
    </div>

    <div class="unlogged" v-if="!isLoggedIn">
      <h1 class="md-display-2">Добро пожаловать в</h1>
      <RouterLink to="/login" class="md-display-1" exact>Lamion</RouterLink>
    </div>
  </article>
</template>

<script>
import api from '@/assets/scripts/api'

export default {
  name: 'HomeAction',
  data: function () {
    return {
      showContent: false,
      appsCount: 0,
      loaded: false
    }
  },
  computed: {
    user () {
      return this.$store.state.user
    },
    isLoggedIn () {
      return (this.$store.state.token != null)
    }
  },
  mounted: async function () {
    if (this.isLoggedIn) {
      const response = await api.apps.count()
      this.appsCount = response.count
    }

    this.loaded = true
  }
}
</script>

<style lang="scss" scoped>

.article {
  height: calc(100vh - 60px);
  display: flex;
  justify-content: center;
  align-items: center;

  .logged {
    display: flex;

    @media screen and (max-width: 618px) {
      & {
        flex-direction: column;
        align-items: center;
        justify-content: center;
      }
    }

    .md-display-2 {
      margin: 0 6px;
    }
  }

  .unlogged {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}

</style>
