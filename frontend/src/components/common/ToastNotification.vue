<script setup>
defineProps({
  show: Boolean,
  message: String
})
</script>

<template>
  <transition
    enter-active-class="transform transition ease-out duration-300"
    enter-from-class="translate-y-2 opacity-0"
    enter-to-class="translate-y-0 opacity-100"
    leave-active-class="transform transition ease-in duration-200"
    leave-from-class="translate-y-0 opacity-100"
    leave-to-class="translate-y-2 opacity-0"
  >
    <div
      v-if="show"
      class="fixed bottom-6 right-6 z-[99999] bg-[#121824] border border-emerald-500/30 text-white px-5 py-3 rounded-2xl shadow-2xl flex items-center space-x-3 backdrop-blur-md"
    >
      <span class="w-2.5 h-2.5 rounded-full bg-emerald-500 animate-pulse"></span>
      <p class="text-xs font-medium">{{ message }}</p>
    </div>
  </transition>
</template>
