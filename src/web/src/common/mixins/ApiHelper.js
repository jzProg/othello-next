const ApiHelper = {
  data() {
    return {
      API_ROOT: '/api/game',
      endpoints: {
        INIT: '/startNewGame',
        CHOOSE: '/choose',
        PLAY: '/play',
        AI: '/getAIMove',
      }
    }
  },
  methods: {
    sendMove(move, successCallback, errorCallback = () => {}, params = {}) {
      this.axios.get(this.API_ROOT + this.endpoints[move], { params })
                  .then(response => successCallback(response))
                  .catch(error => errorCallback(error));
    }
  }
}


export default ApiHelper;
