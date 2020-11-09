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
    }
  },
  methods: {
    choose() {
      this.axios.get(`/api/game/choose`, { params: { playerColor: 'BLACK', gameId: this.gameId }}).then((response) => {
        this.msgs.push(response.data);
      })
    },
    play() {
      this.axios.get(`/api/game/play`, { params: { moveX: 1, moveY: 2, gameId: this.gameId }}).then((response) => {
        this.msgs.push(response.data);
      })
    },
    init() {
      this.axios.get(`/api/game/startNewGame`).then((response) => {
        this.gameId = response.data;
        this.msgs.push(`Game with id=${this.gameId} created!`);
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
