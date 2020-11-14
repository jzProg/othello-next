<template>
  <GameScreen/>
</template>

<script>
import GameScreen from './components/GameScreen.vue'

export default {
  name: 'App',
  components: {
    GameScreen
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

html {
  background-color: black;
}
</style>
