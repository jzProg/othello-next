<template>
  <div class="game-container row">
      <div id="gameDiv" class="text-center">
        <table>
          <tr v-for="row in 8" :key="row">
            <td v-for="column in 8"
                :class="['pieceBox', isAvailableMove(row - 1, column - 1) ? 'highLight' : '' ]"
                :key="column"
                :disabled="!isAvailableMove(row - 1, column - 1) || !!occupied">
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
  import { ref, toRefs, inject } from 'vue';

  export default {
    name: 'GameBoard',
    props: ['gameId', 'board', 'availableMoves'],
    setup(props, context) {
      const { gameId, board, availableMoves } = toRefs(props);

      const colorPerState = {
        EMPTY: 'transparent',
        WHITE: 'white',
        BLACK: 'black'
      }

      const letters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];

      const occupied = ref(false);

      const sendMove = inject('sendMove');

      function lock() {
        occupied.value = true;
      }

      function unlock() {
        occupied.value = false;
      }

      function getPlayerColor(x, y) {
        if (!board.value[x] || !board.value[x][y]) {
          return colorPerState.EMPTY;
        }
        return colorPerState[board.value[x][y]];
      }

      function isAvailableMove(x, y) {
        if (!board.value[x] || board.value[x][y] !== 'EMPTY') return false;
        return availableMoves.value.filter(move => move.x === parseInt(x) && move.y === parseInt(y)).length > 0;
      }

      function play(x, y) {
        if (isAvailableMove(x, y) && occupied.value === false) {
          lock();
          sendMove('PLAY',
            response => { unlock(); context.emit('onplay', response.data) },
            error => { unlock(); context.emit('error', error.response.data, error.response.data.includes('%s')) },
            { moveX: x, moveY: y, gameId: gameId.value });
        }
      }

      return {
         letters,
         occupied,
         getPlayerColor,
         isAvailableMove,
         play
      }
    }
  }
</script>

<style scoped>
  #gameDiv {
    background-color: gray;
  }

  table {
    width: 600px;
    margin: 0 auto;
    margin-top: 2%;
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
