<template>
  <div class="textContainer row">
      <div class="text-center">
        <p v-for="(mes, index) in messages" :key="index">
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
</style>
