import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    meta: { title: 'Главная' },
    component: () => import(/* webpackChunkName: "home" */ '../views/HomeAction.vue')
  },
  {
    path: '/login',
    name: 'Login',
    meta: { title: 'Войти', isAuthenticated: false },
    component: () => import(/* webpackChunkName: "login" */ '../views/LoginAction.vue')
  },
  {
    path: '/register',
    name: 'Registration',
    meta: { title: 'Зарегистрироваться', isAuthenticated: false },
    component: () => import(/* webpackChunkName: "login" */ '../views/RegisterAction.vue')
  },
  {
    path: '/apps',
    name: 'ApplicationsList',
    meta: { title: 'Приложения', isAuthenticated: true },
    component: () => import(/* webpackChunkName: "login" */ '../views/AppListAction.vue')
  },
  {
    path: '/apps/:id',
    name: 'ApplicationDetails',
    meta: { showTitle: false, isAuthenticated: true },
    component: () => import(/* webpackChunkName: "login" */ '../views/AppDetailsAction.vue')
  },
  {
    path: '/app/create',
    name: 'ApplicationCreate',
    meta: { title: 'Новое приложение', isAuthenticated: true },
    component: () => import(/* webpackChunkName: "login" */ '../views/AppCreateAction.vue')
  },
  {
    path: '/apps/:app_id/events/:id',
    name: 'EventsDetails',
    meta: { showTitle: false, isAuthenticated: true },
    component: () => import(/* webpackChunkName: "login" */ '../views/EventDetailsAction.vue')
  },
  {
    path: '*',
    name: 'PageNotFound',
    meta: { title: 'Страница не найдена' },
    component: () => import(/* webpackChunkName: "404" */ '../views/PageNotFound.vue')
  }
]

const router = new VueRouter({
  base: process.env.BASE_URL,
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  if ((typeof to.meta.isAuthenticated === 'undefined' || to.meta.isAuthenticated === null) ||
  (to.meta.isAuthenticated === (localStorage.getItem('token') !== null))) {
    next()
  } else {
    next({
      name: 'Home',
      replace: true
    })
  }
})

router.beforeEach((to, from, next) => {
  if (to.meta.showTitle !== false) {
    store.commit('setPageTitle', to.meta.title)
  }

  next()
})

export default router
