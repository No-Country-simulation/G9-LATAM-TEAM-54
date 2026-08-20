<script setup>
defineProps({
  currentTab: String,
  generandoReporte: Boolean
})

const emit = defineEmits(["generar-reporte", "logout"])
</script>

<template>
  <header class="h-16 shrink-0 border-b border-white/5 px-8 flex justify-between items-center bg-[#0d111a]/90 backdrop-blur-md z-10">
    <div>
      <h1 class="text-xs font-bold uppercase tracking-widest text-slate-400">
        Dashboard / <span class="text-white">{{ currentTab }}</span>
      </h1>
    </div>
    <div class="flex items-center space-x-4">
      <button
        v-if="currentTab === 'dashboard'"
        @click="emit('generar-reporte')"
        :disabled="generandoReporte"
        class="px-3.5 py-1.5 bg-emerald-500/10 hover:bg-emerald-500/20 border border-emerald-500/30 text-emerald-400 rounded-xl font-bold text-xs transition flex items-center space-x-1.5 shadow-sm disabled:opacity-50"
      >
        <span>⚡</span>
        <span>{{ generandoReporte ? "Generando..." : "Generar Reporte" }}</span>
      </button>
      <div :class="currentTab === 'dashboard' ? 'pl-3 border-l border-white/10' : ''" class="flex items-center space-x-2">
        <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
        <button @click="emit('logout')" class="text-xs text-rose-400 hover:underline font-semibold">Salir</button>
      </div>
    </div>
  </header>
</template>
