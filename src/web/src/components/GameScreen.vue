<template>
  <div class="game">
    <p v-for='(msg, index) in msgs' :key="index">{{ msg }}</p>
    <button @click.prevent="init()">New Game</button>
    <button @click.prevent="choose()">Choose Color(black-first)</button>
    <button @click.prevent="play()">Play</button>
  </div>
</template>

<script>
export default {
  name: 'GameScreen',
  data() {
    return {
      msgs: [],
      gameId: null,
      stompClient: null,
      socket: null,
      activeSubscription: null,
    }
  },
  created () {
    this.connectToSocket();
  },
  methods: {
    connectToSocket() {
      // eslint-disable-next-line
      this.socket = new SockJS('/ws-messaging');
      // eslint-disable-next-line
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
      }, () => {
        console.log('Disconnect! Retrying connection...');
        this.connectToSocket();
      });
    },
    choose() {
      this.stompClient.send(`/app/choose/${this.gameId}`, {}, JSON.stringify({playerColor: 'BLACK'}));
    },
    play() {
      this.stompClient.send(`/app/play/${this.gameId}`, {}, JSON.stringify({moveX: 1, moveY: 2}));
    },
    init() {
      this.axios.get(`/api/game/startNewGame`).then((response) => {
        this.gameId = response.data;
        this.msgs.push(`Game with id=${this.gameId} created!`);
        this.stompClient.subscribe(`/topic/game/${this.gameId}`, (message) => {
          this.msgs.push(JSON.parse(message.body));
        });
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
