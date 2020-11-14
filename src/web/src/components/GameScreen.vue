<template>
  <div class="board container">
    <div id= "turnRow" class="row" style="background-color: black;min-height: 80px">
        <div id="titleDiv" class="col-md-3" style="color: lightblue;">Othello Riversi</div>
        <div id="turnDiv" class="col-md-3 text-center" v-show="!showOptions">{{ player }} player</div>
        <div id="scoreDiv" class="col-md-3 text-center" v-show="!showOptions" style="color: white">
          <i class="fas fa-circle" style="borderColor: white; border-radius:20px; border-style:solid; color: black"/> {{ score.BLACK }}
          <i class="fas fa-circle" style="borderColor: white; border-radius:20px; border-style:solid; color: white"/> {{ score.WHITE }}
        </div>
        <div id="resetDiv" class="col-md-3 text-center" v-show="!showOptions">
          <a @click.prevent="reset()" class="reset">
            New Game <i class="fas fa-redo"></i>
          </a>
        </div>
    </div>
    <Options v-if="showOptions" :options="options" @select="choose" @close="choose" />
    <template v-else>
      <GameBoard :gameId="gameId" />
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
        score: {
          BLACK: 0,
          WHITE: 0
        },
        options: [ 'BLACK', 'WHITE'],
        player: 'Black',
      }
    },
    created() {
      this.init();
    },
    methods: {
      choose(color) {
        this.axios.get(`/api/game/choose`, { params: { playerColor: color || this.options[0], gameId: this.gameId }}).then((response) => {
          this.showOptions = false;
          this.msgs.push(response.data.gameMessage);
        })
      },
      init() {
        this.axios.get(`/api/game/startNewGame`).then((response) => {
          this.gameId = response.data.gameId;
          this.msgs.push(response.data.gameMessage);
          this.showOptions = true;
        })
      },
      reset() {
        this.msgs = []
        this.init();
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
</style>
