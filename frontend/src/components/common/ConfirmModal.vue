<script setup>
defineProps({
  visible: Boolean,
  title: {
    type: String,
    default: "¿Confirmar acción?"
  },
  message: {
    type: String,
    default: "¿Estás seguro de que deseas realizar esta acción?"
  },
  itemName: {
    type: String,
    default: ""
  },
  loading: Boolean,
  confirmButtonText: {
    type: String,
    default: "Eliminar"
  }
})

const emit = defineEmits(["confirm", "cancel"])
</script>

<template>
  <transition
    enter-active-class="transition duration-300 ease-out"
    enter-from-class="opacity-0"
    enter-to-class="opacity-100"
    leave-active-class="transition duration-200 ease-in"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="visible"
      class="fixed inset-0 z-[9999] bg-black/75 backdrop-blur-md flex items-center justify-center p-4 w-screen h-screen"
      @click.self="emit('cancel')"
    >
      <transition
        enter-active-class="transition duration-300 ease-out transform"
        enter-from-class="opacity-0 scale-95 translate-y-3"
        enter-to-class="opacity-100 scale-100 translate-y-0"
        leave-active-class="transition duration-200 ease-in transform"
        leave-from-class="opacity-100 scale-100 translate-y-0"
        leave-to-class="opacity-0 scale-95 translate-y-3"
        appear
      >
        <div class="w-full max-w-sm bg-[#121824] border border-rose-500/30 shadow-2xl shadow-rose-950/40 p-6 rounded-2xl space-y-4 text-center backdrop-blur-xl relative z-[10000]">
          <div class="w-12 h-12 rounded-2xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center mx-auto text-rose-400 text-xl shadow-inner">
            🗑️
          </div>
          <div>
            <h3 class="text-base font-extrabold text-white tracking-tight">{{ title }}</h3>
            <p class="text-xs text-slate-400 mt-2 leading-relaxed">
              {{ message }} <span v-if="itemName" class="text-rose-300 font-semibold font-mono">{{ itemName }}</span>
            </p>
          </div>
          <div class="flex items-center space-x-3 pt-2">
            <button
              type="button"
              @click="emit('cancel')"
              class="flex-1 px-4 py-2.5 bg-white/5 hover:bg-white/10 border border-white/10 text-slate-300 font-bold rounded-xl text-xs transition"
            >
              Cancelar
            </button>
            <button
              type="button"
              @click="emit('confirm')"
              :disabled="loading"
              class="flex-1 px-4 py-2.5 bg-rose-500 hover:bg-rose-600 text-white font-bold rounded-xl text-xs transition shadow-lg shadow-rose-500/20 disabled:opacity-50 flex items-center justify-center space-x-1.5"
            >
              <span>{{ loading ? "Eliminando..." : confirmButtonText }}</span>
            </button>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>
