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
  import { ref, provide } from 'vue';
  import Options from '@/components/modals/ChooseColor';
  import GameBoard from '@/components/GameBoard';
  import TextUI from '@/components/TextUI';
  import GameStatusBar from '@/components/GameStatusBar';
  import ErrorModal from '@/components/modals/ErrorModal';
  import PostGame from '@/components/modals/PostGame';
  import useApiHelper from '@/common/ApiHelper';

  export default {
    name: 'GameScreen',
    components: { GameBoard, GameStatusBar, Options, TextUI, ErrorModal, PostGame },
    setup() {
      const options = [ 'BLACK', 'WHITE'];
      const levels = ['EASY', 'DIFFICULT'];
      const loseTurnMessage = 'No moves Available. You lose turn...';

      const playerColor = ref('');
      const msgs = ref([]);
      const game_id = ref(null);
      const showOptions = ref(true);
      const playerTurn = ref('');
      const game_board = ref([]);
      const showError = ref(false);
      const loseTurn = ref(false);
      const level = ref('');
      const game_score = ref([]);
      const available_moves = ref([]);
      const gameFinished = ref(false);

      const { sendMove } = useApiHelper();

      provide('sendMove', sendMove);

      init();

      function init() { // mounted ??
        sendMove('INIT', (response) => {
          const { gameId, gameMessage, playerToMove, board }  = response.data;
          game_id.value = gameId;
          game_board.value = board;
          playerTurn.value = playerToMove.toLowerCase();
          msgs.value.push(gameMessage);
          showOptions.value = true;
        });
      }

      function onError(error, loseTurn = false) {
        msgs.value.push(error.response.data.replace('%s', playerTurn.value));
        showError.value = true;
        loseTurn.value = loseTurn;
      }

      function onPlayed(response) {
        available_moves.value = [];
        const { gameMessage, playerToMove, board, score, result, availableMoves }  = response;
        msgs.value.push(gameMessage);
        playerTurn.value = playerToMove.toLowerCase();
        game_board.value = board;
        game_score.value = score;
        updatePlayerAvailableMoves(playerToMove, availableMoves);
        if (result) {
          gameFinished.value = true;
          msgs.value.push(result);
        }
        if (!gameFinished.value && playerToMove !== playerColor.value) getAIMove();
      }

      function onErrorConfirm() {
        showError.value = false;
        afterErrorAction();
      }

      function afterErrorAction() {
        if (loseTurn.value) {
          loseTurn.value = false; //reset
          getAIMove();
        }
      }

      function reset() {
        gameFinished.value = false;
        msgs.value = [];
        init();
      }

      function updatePlayerAvailableMoves(playerToMove, availableMoves) {
        if (playerToMove === playerColor.value) available_moves.value = availableMoves;
      }

      function choose(choices) {
        playerColor.value = choices.color || options[0]; // black -> easy
        level.value = choices.level || levels[0]; // default -> easy
        playerTurn.value = options[0]; // black always first
        sendMove('CHOOSE', function (response) {
          showOptions.value = false;
          const { gameMessage, score, availableMoves } = response.data;
          msgs.value.push(gameMessage);
          game_score.value = score;
          available_moves.value = availableMoves;
          if (playerColor.value === options[1]) getAIMove(); // if user chose WHITE, AI plays first
        }, () => {}, { playerColor: playerColor.value, gameId: game_id.value, gameLevel: level.value });
      }

      function getAIMove() {
        available_moves.value = [];
        setTimeout(() => {
          sendMove('AI', (response) => {
              const { gameMessage, playerToMove, board, score, availableMoves, result } = response.data;
              msgs.value.push(gameMessage);
              playerTurn.value = playerToMove.toLowerCase();
              game_board.value = board;
              game_score.value = score;
              updatePlayerAvailableMoves(playerToMove, availableMoves);
              if (result) {
                gameFinished.value = true;
                msgs.value.push(result);
              }
              if (!gameFinished.value && playerToMove !== playerColor.value) onError({ response: { data : loseTurnMessage }}, true); // if no available moves for the user, AI plays again but first inform user
          }, onError.value);
        }, 1000);
      }

      return {
        options,
        levels,
        playerColor,
        msgs,
        gameId : game_id,
        showOptions,
        playerTurn,
        board : game_board,
        showError,
        loseTurn,
        level,
        score : game_score,
        availableMoves : available_moves,
        gameFinished,
        onError,
        onPlayed,
        onErrorConfirm,
        afterErrorAction,
        reset,
        updatePlayerAvailableMoves,
        choose,
        getAIMove
      }
    },
  }
</script>

<style scoped>
  .board {
    width:100%;
    background-color: gray;
    min-height: 800px
  }
</style>
