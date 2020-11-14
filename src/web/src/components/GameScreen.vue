<template>
  <div class="board container">
    <div id= "turnRow" class="row" style="background-color: black;min-height: 80px">
        <div id="titleDiv" class="col-md-3" style="color: lightblue;">Othello Riversi</div>
        <div id="turnDiv" class="col-md-3 text-center" v-show="!showOptions">{{ player }} player</div>
        <div id="scoreDiv" class="col-md-3 text-center" v-show="!showOptions" style="color: white">
          <i class="fas fa-circle" style="borderColor: white; border-radius:20px; border-style:solid; color: black"/> {{ getScore[0] }}
          <i class="fas fa-circle" style="borderColor: white; border-radius:20px; border-style:solid; color: white"/> {{ getScore[1] }}
        </div>
        <div id="resetDiv" class="col-md-3 text-center" v-show="!showOptions">
          <a @click.prevent="reset()" class="reset">
            New Game <i class="fas fa-redo"></i>
          </a>
        </div>
    </div>
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

  export default {
    name: 'GameScreen',
    components: { GameBoard, Options, TextUI },
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
   #turnDiv{
      margin-left: 3%;
      background-color: green;
      color: white;
      min-height: 30px;
      width: 150px;
      padding: 10px;
      margin-top: 1%;
      border-radius: 7%;
      font-size: 150%;
   }

   #scoreDiv{
      margin-left: 3%;
      margin-top: 1%;
      font-size: 200%;
   }

   #resetDiv{
      margin-left: 3%;
      margin-top: 1%;
      font-size: 200%;
   }

   #titleDiv{
      margin-top: 1%;
      font-size: 250%;
   }

   #seconds{
      color: white;
   }

  .board {
    width:100%;
    background-color: gray;
    min-height: 800px
  }

  .reset {
    color: red;
    text-decoration: none;
    cursor: pointer;
  }
</style>
