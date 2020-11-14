<template>
  <div class="board container">
    <GameStatusBar :score-info="getScore" :player="player" :render-game-info="showOptions" @new-game="reset"/>
    <Options v-if="showOptions" :options="options" @select="choose" @close="choose" />
    <template v-else>
      <GameBoard :gameId="gameId" :board="board" @played="onPlayed"/>
      <TextUI :messages="msgs" />
    </template>
  </div>
</template>

<script>
  import Options from '@/components/modals/ChooseColor';
  import GameBoard from '@/components/GameBoard';
  import TextUI from '@/components/TextUI';
  import GameStatusBar from '@/components/GameStatusBar';

  export default {
    name: 'GameScreen',
    components: { GameBoard, GameStatusBar, Options, TextUI },
    data() {
      return {
        msgs: [],
        gameId: null,
        showOptions: false,
        options: [ 'BLACK', 'WHITE'],
        player: 'black',
        board: [],
      }
    },
    created() {
     this.init();
    },
    methods: {
      onPlayed(result) {
        // eslint-disable-next-line
        const { gameMessage, moveX, moveY, playerToMove, board }  = result;
        this.msgs.push(gameMessage);
        this.player = playerToMove.toLowerCase();
        this.board = board;
        // TODO handle AI move
      },
      choose(color) {
        this.axios.get(`/api/game/choose`, { params: { playerColor: color || this.options[0], gameId: this.gameId }}).then((response) => {
          this.showOptions = false;
          this.msgs.push(response.data.gameMessage);
          // TODO handle AI move if user choose WHITE
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
    },
    computed: {
      getScore() {
        const flatArray = this.board.flatMap(x => x.filter(a => a !== 'EMPTY'));
        const blackCount = flatArray.filter(a => a === 'BLACK').length;
        const whiteCount = flatArray.length - blackCount;
        return [blackCount, whiteCount];
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
