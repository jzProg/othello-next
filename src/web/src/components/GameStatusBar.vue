<template>
  <div id= "turnRow" class="row">
      <div id="titleDiv" class="col-md-3" style="color: lightblue">Othello Riversi</div>
      <div id="turnDiv" class="col-md-3 text-center" v-show="!renderGameInfo">{{ player }} player</div>
      <div id="scoreDiv" class="col-md-3 text-center" v-show="!renderGameInfo" style="color: white">
        <i class="fas fa-circle" style="borderColor: white; border-radius:20px; border-style:solid; color: black"/> {{ scoreInfo[0] }}
        <i class="fas fa-circle" style="borderColor: white; border-radius:20px; border-style:solid; color: white"/> {{ scoreInfo[1] }}
      </div>
      <div id="resetDiv" class="col-md-3 text-center" v-show="!renderGameInfo">
        <a @click.prevent="reset()" class="reset">
          New Game <i class="fas fa-redo"></i>
        </a>
      </div>
  </div>
</template>

<script>
  export default {
    name: 'GameStatusBar',
    props: ['scoreInfo', 'player', 'renderGameInfo'],
    methods: {
      reset() {
        this.$emit('new-game');
      }
    }
  }
</script>

<style scoped>
  #turnRow{
    background-color: black;
    min-height: 80px;
  }

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

 .reset {
   color: red;
   text-decoration: none;
   cursor: pointer;
 }
</style>
