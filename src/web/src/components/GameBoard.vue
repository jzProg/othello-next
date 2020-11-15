<template>
  <div class="game-container row">
      <div id="gameDiv" class="text-center">
        <table style='width:600px;margin: 0 auto;margin-top: 5%'>
          <tr v-for="row in 8" :key="row">
            <td v-for="column in 8" :class="['pieceBox', isAvailableMove(row - 1, column - 1) ? 'highLight' : '' ]" :key="column">
              <i @click.prevent="play(row - 1, column - 1)"
                 class="fas fa-circle fa-4x"
                 :style="{ color: getPlayerColor(row - 1, column - 1) }"/>
            </td>
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
    props: ['gameId', 'board', 'availableMoves'],
    data() {
      return {
        colorPerState: {
          EMPTY: 'transparent',
          WHITE: 'white',
          BLACK: 'black'
        },
        letters: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'],
      }
    },
    methods: {
      play(x, y) {
        this.axios.get('/api/game/play', { params: { moveX: x, moveY: y, gameId: this.gameId }}).then((response) => {
          this.$emit('played', response.data);
        }).catch((error) => {
          this.$emit('error', error.response.data, error.response.data.includes('%s'));
        });
      },
      isAvailableMove(x, y) {
        return this.availableMoves.filter(move => move.x === parseInt(x) && move.y === parseInt(y)).length > 0;
      },
      getPlayerColor(x, y) {
        if (!this.board[x] || !this.board[x][y]) {
          return this.colorPerState.EMPTY;
        }
        return this.colorPerState[this.board[x][y]];
      }
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
    cursor: pointer;
  }

  .pieceBox:hover {
    opacity: 0.5
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

  .highLight {
    background-color: yellow;
  }
</style>
