// store.js
import { defineStore } from 'pinia';

export const useJacobiStore = defineStore('jacobi', {
    state: () => ({
        lastResult: null,
    }),
    actions: {
        setLastResult(result) {
            this.lastResult = result;
        },
        clearLastResult() {
            this.lastResult = null;
        },
    },
});
