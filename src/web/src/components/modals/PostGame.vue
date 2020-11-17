<template>
  <Modal>
    <template v-slot:[closeButton]>
      <span id='closeSymbol' @click.prevent="close">x</span>
    </template>
    <br>
    <template v-slot:[header]>
      <h3>Game Ended</h3>
    </template>
    <template v-slot:[body]>
      <div class="container" style="width: 100%">
         <div class="row">
            <p>{{ message }}</p>
         </div>
      </div>
   </template>
   <template v-slot:[footer]>
     <div class="text-center">
       <button class="btn btn-primary" @click.prevent="play">Play again</button>
     </div>
  </template>
  </Modal>
</template>

<script>
import Modal from '@/components/modals/GenericModal';

export default {
  name: 'PostGame',
  props: ['message'],
  components: { Modal },
  data() {
    return {
      header: 'header',
      closeButton: 'close',
      body: 'body',
      footer: 'footer'
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
    play() {
      this.$emit('retry');
    }
  }
}
</script>
