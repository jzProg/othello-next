<template>
  <div class="board container">
    <GameStatusBar :score-info="score" :player="playerTurn" :render-game-info="showOptions" @new-game="reset"/>
    <Options v-if="showOptions" :options="options" :levels="levels" @select="choose" @close="choose" />
    <template v-else>
      <GameBoard :gameId="gameId" :board="board" :available-moves="availableMoves" @onplay="onPlayed" @error="onError"/>
      <TextUI :messages="msgs" />
    </template>
    <ErrorModal v-if="showError" :message="msgs[msgs.length - 1]" @close="onErrorConfirm" />
    <PostGame v-if="gameFinished" :message="msgs[msgs.length - 1]" @retry="reset" @close="reset" />
  </div>
</template>

<script>
  import Options from '@/components/modals/ChooseColor';
  import GameBoard from '@/components/GameBoard';
  import TextUI from '@/components/TextUI';
  import GameStatusBar from '@/components/GameStatusBar';
  import ErrorModal from '@/components/modals/ErrorModal';
  import PostGame from '@/components/modals/PostGame';
  import ApiHelper from '@/common/mixins/ApiHelper';

  export default {
    name: 'GameScreen',
    components: { GameBoard, GameStatusBar, Options, TextUI, ErrorModal, PostGame },
    mixins: [ApiHelper],
    data() {
      return {
        playerColor: '',
        msgs: [],
        gameId: null,
        showOptions: false,
        options: [ 'BLACK', 'WHITE'],
        levels: ['EASY', 'DIFFICULT'],
        playerTurn: '',
        board: [],
        showError: false,
        loseTurn: false,
        score: [],
        level: '',
        availableMoves: [],
        gameFinished: false,
        loseTurnMessage: 'No moves Available. You lose turn...',
      }
    },
    created() {
     this.init();
    },
    methods: {
      onError(error, loseTurn = false) {
        this.msgs.push(error.response.data.replace('%s', this.playerTurn));
        this.showError = true;
        this.loseTurn = loseTurn;
      },
      onPlayed(response) {
        this.availableMoves = [];
        const { gameMessage, playerToMove, board, score, result, availableMoves }  = response;
        this.msgs.push(gameMessage);
        this.playerTurn = playerToMove.toLowerCase();
        this.board = board;
        this.score = score;
        this.updatePlayerAvailableMoves(playerToMove, availableMoves);
        if (result) {
          this.gameFinished = true;
          this.msgs.push(result);
        }
        if (!this.gameFinished && playerToMove !== this.playerColor) this.getAIMove();
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
          this.sendMove('AI', (response) => {
              const { gameMessage, playerToMove, board, score, availableMoves, result } = response.data;
              this.msgs.push(gameMessage);
              this.playerTurn = playerToMove.toLowerCase();
              this.board = board;
              this.score = score;
              this.updatePlayerAvailableMoves(playerToMove, availableMoves);
              if (result) {
                this.gameFinished = true;
                this.msgs.push(result);
              }
              if (!this.gameFinished && playerToMove !== this.playerColor) this.onError({ response: { data : this.loseTurnMessage }}, true); // if no available moves for the user, AI plays again but first inform user
          }, this.onError);
        }, 1000);
      },
      choose(choices) {
        this.playerColor = choices.color || this.options[0]; // black -> easy
        this.level = choices.level || this.levels[0]; // default -> easy
        this.playerTurn = this.options[0]; // black always first
        this.sendMove('CHOOSE', (response) => {
          this.showOptions = false;
          const { gameMessage, score, availableMoves } = response.data;
          this.msgs.push(gameMessage);
          this.score = score;
          this.availableMoves = availableMoves;
          if (this.playerColor === this.options[1]) this.getAIMove(); // if user chose WHITE, AI plays first
        }, () => {}, { playerColor: this.playerColor, gameId: this.gameId, gameLevel: this.level });
      },
      init() {
        this.sendMove('INIT', (response) => {
          const { gameId, gameMessage, playerToMove, board }  = response.data;
          this.gameId = gameId;
          this.board = board;
          this.playerTurn = playerToMove.toLowerCase();
          this.msgs.push(gameMessage);
          this.showOptions = true;
        });
      },
      reset() {
        this.gameFinished = false;
        this.msgs = [];
        this.init();
      },
      updatePlayerAvailableMoves(playerToMove, availableMoves) {
        if (playerToMove === this.playerColor) this.availableMoves = availableMoves;
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
