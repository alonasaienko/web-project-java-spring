import { ref } from 'vue';

export const EventBus = {
  event: ref(null),

  emit(event) {
    this.event.value = event;
  },

  on(callback) {
    return this.event.value && callback(this.event.value);
  }
};
