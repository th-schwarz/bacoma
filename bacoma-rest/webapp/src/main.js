var app = new Vue({
  el: '#app',
  data () {
	    return {
	      info: null
	    }
	  },
	  mounted () {
	    axios
	      .get('http://localhost:8080/getAll/')
	      .then(response => (this.info = response))
	  }
})