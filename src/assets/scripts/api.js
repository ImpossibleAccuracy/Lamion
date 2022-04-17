import config from '../../config'

const serializeQueryString = function (obj) {
  const str = []
  for (const p in obj) {
    if (Object.prototype.hasOwnProperty.call(obj, p)) {
      str.push(encodeURIComponent(p) + '=' + encodeURIComponent(obj[p]))
    }
  }
  return str.join('&')
}

const normalizeQuery = function (params) {
  if (!params) {
    params = {}
  }

  if (params.method !== 'GET') {
    params.body = JSON.stringify(params.body)
  }

  if (!params.method) {
    params.method = 'GET'
  }

  if (params.headers == null) {
    params.headers = {}
  }
  if (!params.headers['Content-Type']) {
    params.headers = {
      'Content-Type': 'application/json'
    }
  }

  if (!params.headers.Authorization) {
    params.headers.Authorization = localStorage.getItem('token')
  }

  return params
}

const query = async function (url, params) {
  params = normalizeQuery(params)

  let request
  if (params.method === 'GET') {
    request = fetch.bind(null, config.baseApiUrl + url + '?' + serializeQueryString(params.body), params)
  } else {
    request = fetch.bind(null, config.baseApiUrl + url, params)
  }

  const response = await request()
  let body = await response.text()
  try {
    body = JSON.parse(body)
  } catch (error) {}

  if (response.ok) {
    return body
  } else {
    const result = new Error(body.details)
    result.ok = response.ok
    result.status = response.status
    result.statusText = response.statusText
    result.redirected = response.redirected
    result.url = response.url
    throw result
  }
}

export default {
  auth: {
    login: function (email, password) {
      return query.call(this, '/login', {
        method: 'POST',
        body: {
          email: email,
          password: password
        }
      })
    },
    register: function (email, password) {
      return query.call(this, '/register', {
        method: 'POST',
        body: {
          email: email,
          password: password
        }
      })
    }
  },
  user: {
    load: function (token) {
      return query.call(this, '/user', {
        method: 'GET',
        headers: {
          Authorization: token
        }
      })
    }
  },
  apps: {
    create: function (app) {
      return query.call(this, '/application/', {
        method: 'POST',
        body: app
      })
    },
    load: function () {
      return query.call(this, '/application')
    },
    count: function () {
      return query.call(this, '/application/count')
    },
    get: function (id) {
      return query.call(this, `/application/${id}`)
    },
    update: function (id, app) {
      return query.call(this, `/application/${id}`, {
        method: 'PUT',
        body: app
      })
    },
    delete: function (id) {
      return query.call(this, `/application/${id}`, {
        method: 'DELETE'
      })
    }
  },
  events: {
    create: function (appId, event) {
      return query.call(this, `/application/${appId}/event`, {
        method: 'POST',
        body: event
      })
    },
    load: function (appId) {
      return query.call(this, `/application/${appId}/event`)
    },
    get: function (appId, id) {
      return query.call(this, `/application/${appId}/event/${id}`)
    },
    delete: function (appId, id) {
      return query.call(this, `/application/${appId}/event/${id}`, {
        method: 'DELETE'
      })
    }
  },
  requests: {
    load: function (appId, eventId) {
      return query.call(this, `/application/${appId}/event/${eventId}/request`)
    },
    loadAnalytics: function (appId, eventId) {
      return query.call(this, `/application/${appId}/event/${eventId}/request/analytics`)
    }
  }
}
