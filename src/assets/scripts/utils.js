
const utils = {
  random: function (length) {
    return Math.floor(Math.random() * length)
  },
  toLower: text => {
    return text.toString().toLowerCase()
  },
  searchByName: (items, term) => {
    if (term) {
      return items.filter(item => utils.toLower(item.title).includes(utils.toLower(term)))
    }

    return items
  },
  buildDateString: function (date) {
    return date.toUTCString()
  }
}

export default utils
