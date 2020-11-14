<template>
  <Modal :content-color="'green'">
    <template v-slot:[closeButton]>
      <span id='closeSymbol'
        @click.prevent="close">x</span>
    </template>
    <br>
    <template v-slot:[header]>
      <h3>Choose Color</h3>
    </template>
    <template v-slot:[body]>
      <div class="container" style="width: 100%">
         <div class="row">
              <i v-for="(option, index) in options"
                :key="index"
                class="col-md-6 fas fa-circle fa-5x piece"
                :style="getStyle(option)"
                @click.prevent="choose(option)"/>
         </div>
    </div>
   </template>
  </Modal>
</template>

<script>
import Modal from '@/components/modals/GenericModal';

export default {
  name: 'ChooseColor',
  props: ['options'],
  components: { Modal },
  data() {
    return {
      header: 'header',
      closeButton: 'close',
      body: 'body',
    }
  },
  methods: {
    getStyle(color) {
      return { color: color.toLowerCase() };
    },
    choose(color) {
      this.$emit('select', color);
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

 .piece:hover {
   border-color: yellow;
   opacity: 1.0;
 }
</style>
