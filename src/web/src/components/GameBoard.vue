<template>
  <div class="game-container row">
      <div id="gameDiv" class="text-center">
        <table style='width:600px;margin: 0 auto;margin-top: 5%'>
          <tr v-for="row in 8" :key="row">
            <td v-for="column in 8" class="pieceBox" :key="column"/>
            <td class="numberBox">
              <h3><b>{{ row }}</b></h3>
            </td>
          </tr>
          <tr>
            <td v-for="column in 8" class="letterBox" :key="column">
              <h3><b>{{ letters[column - 1] }}</b></h3>
            </td>
          </tr>
        </table>
      </div>
  </div>
</template>

<script>
  export default {
    name: 'GameBoard',
    props: ['gameId'],
    data() {
      return {
        letters: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
      }
    },
    created() {
    },
    methods: {
      play() {
        this.axios.get(`/api/game/play`, { params: { moveX: 1, moveY: 2, gameId: this.gameId }}).then((response) => {
          this.msgs.push(response.data.gameMessage);
        })
      },
    }
  }
</script>

<style scoped>
  #gameDiv {
    background-color: gray;
    padding-top: 1%;
  }

  .pieceBox {
    background-color: green;
    height: 70px;
    width: 150px;
    border-style: solid;
    border-color: white;
  }

  .letterBox {
    background-color: gray;
    height: 70px;
    width: 150px;
  }

  .numberBox {
    background-color: gray;
    height: 70px;
    width: 150px;
  }
</style>
