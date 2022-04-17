
export default {
  applicationName: 'Lamion',
  baseApiUrl: 'http://127.0.0.1:8080/api',
  user: {
    password: {
      minLength: 6,
      maxLength: 255
    }
  },
  app: {
    title: {
      maxLength: 255
    },
    description: {
      maxLength: 1024
    }
  },
  request: {
    title: {
      default: 'Имя не указано'
    }
  },
  chart: {
    label: {
      maxLength: 30
    }
  }
}
