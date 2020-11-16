<template>
  <div class="textContainer row">
      <div id="contentDiv" class="text-center scrollable">
        <p v-for="(mes, index) in messages.slice().reverse()" :key="index">
          <i style="color: green"> {{ new Date().toLocaleTimeString() }}</i> {{ mes }} </p>
      </div>
  </div>
</template>

<script>
  export default {
    name: 'textUI',
    props: ['messages'],
  }
</script>

<style scoped>
  .textContainer{
    background-color: black;
    color: white;
    padding: 2%;
  }

  .scrollable {
    height: 300px;
    overflow-y:auto;
    max-width: 100%;
    overflow-x: hidden;
  }
</style>
