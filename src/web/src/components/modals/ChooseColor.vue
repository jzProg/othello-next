<template>
  <Modal :content-color="'green'">
    <template v-slot:[closeButton]>
      <span id='closeSymbol' @click.prevent="close">x</span>
    </template>
    <br>
    <template v-slot:[header]>
      <h3>Choose Game Info</h3>
    </template>
    <template v-slot:[body]>
      <div class="container" style="width: 100%">
         <div class="row">
              <i v-for="(option, index) in options"
                :key="index"
                class="col-md-6 fas fa-circle fa-5x piece"
                :style="getStyle(option)"
                @click.prevent="selectColor(option)"/>
         </div>
    </div>
   </template>
   <template v-slot:[footer]>
     <div class="text-center">
       <span v-for="(level, index) in levels" :key="index">
          <input  type="checkbox" :id="`checkbox_${index}`" @click="selectLevel(level)">
          <label :for="`checkbox_${index}`">{{ level }}</label><br>
       </span>
      <button class="btn btn-primary" @click.prevent="choose()" :disabled="!selectedLevel || !selectedColor">Start</button>
     </div>
   </template>
  </Modal>
</template>

<script>
import Modal from '@/components/modals/GenericModal';

export default {
  name: 'ChooseColor',
  props: ['options', 'levels'],
  components: { Modal },
  data() {
    return {
      header: 'header',
      closeButton: 'close',
      body: 'body',
      footer: 'footer',
      selectedLevel: '',
      selectedColor: '',
    }
  },
  methods: {
    selectLevel(level) {
      this.selectedLevel = level;
    },
    selectColor(color) {
      this.selectedColor = color;
    },
    getStyle(color) {
      return { color: color.toLowerCase(), opacity: this.selectedColor === color ? '1' : '0.5' };
    },
    choose() {
      this.$emit('select', { color: this.selectedColor, level: this.selectedLevel });
    },
    close() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>
 .piece {
   cursor: pointer;
   opacity: 0.5;
 }
</style>
