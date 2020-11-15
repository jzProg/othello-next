<template>
  <div class="board container">
    <GameStatusBar :score-info="score" :player="player" :render-game-info="showOptions" @new-game="reset"/>
    <Options v-if="showOptions" :options="options" @select="choose" @close="choose" />
    <template v-else>
      <GameBoard :gameId="gameId" :board="board" :available-moves="availableMoves" @played="onPlayed" @error="onError"/>
      <TextUI :messages="msgs" />
    </template>
    <ErrorModal v-if="showError" :message="msgs[msgs.length - 1]" @close="onErrorConfirm" />
  </div>
</template>

<script>
  import Options from '@/components/modals/ChooseColor';
  import GameBoard from '@/components/GameBoard';
  import TextUI from '@/components/TextUI';
  import GameStatusBar from '@/components/GameStatusBar';
  import ErrorModal from '@/components/modals/ErrorModal';

  export default {
    name: 'GameScreen',
    components: { GameBoard, GameStatusBar, Options, TextUI, ErrorModal },
    data() {
      return {
        msgs: [],
        gameId: null,
        showOptions: false,
        options: [ 'BLACK', 'WHITE'],
        player: 'black',
        board: [],
        showError: false,
        loseTurn: false,
        score: [],
        availableMoves: [],
      }
    },
    created() {
     this.init();
    },
    methods: {
      onError(error, loseTurn = false) {
        this.msgs.push(error.replace('%s', this.player));
        this.showError = true;
        this.loseTurn = loseTurn;
      },
      onPlayed(result) {
        this.availableMoves = [];
        const { gameMessage, playerToMove, board, score }  = result;
        this.msgs.push(gameMessage);
        this.player = playerToMove.toLowerCase();
        this.board = board;
        this.score = score;
        this.getAIMove();
      },
      onErrorConfirm() {
        this.showError = false;
        this.afterErrorAction();
      },
      afterErrorAction() {
        if (this.loseTurn) {
          this.loseTurn = false; //reset
          this.getAIMove();
        }
      },
      getAIMove() {
        setTimeout(() => {
          this.axios.get('/api/game/getAIMove').then((response) => {
              const { gameMessage, playerToMove, board, score, availableMoves } = response.data;
              this.msgs.push(gameMessage);
              this.player = playerToMove.toLowerCase();
              this.board = board;
              this.score = score;
              this.availableMoves = availableMoves;
          }).catch((error) => {
            this.onError(error.response.data);
          });
        }, 1000);
      },
      choose(color) {
        this.axios.get('/api/game/choose', { params: { playerColor: color || this.options[0], gameId: this.gameId }}).then((response) => {
          this.showOptions = false;
          const { gameMessage, score, availableMoves } = response.data;
          this.msgs.push(gameMessage);
          this.score = score;
          this.availableMoves = availableMoves;
          if (color === this.options[1]) this.getAIMove(); // if user chose WHITE, AI plays first
        })
      },
      init() {
        this.axios.get(`/api/game/startNewGame`).then((response) => {
          const { gameId, gameMessage, playerToMove, board }  = response.data;
          this.gameId = gameId;
          this.board = board;
          this.player = playerToMove.toLowerCase();
          this.msgs.push(gameMessage);
          this.showOptions = true;
        })
      },
      reset() {
        this.msgs = [];
        this.init();
      }
    }
  }
</script>

<style scoped>
  .board {
    width:100%;
    background-color: gray;
    min-height: 800px
  }
</style>
