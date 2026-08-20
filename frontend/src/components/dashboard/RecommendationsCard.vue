<script setup>
defineProps({
  recomendaciones: {
    type: Array,
    default: () => []
  }
})
</script>

<template>
  <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col justify-between">
    <div>
      <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest mb-4">Recomendaciones IA</h2>
      <div class="space-y-3">
        <div
          v-for="(rec, index) in recomendaciones"
          :key="index"
          class="p-3.5 bg-[#090d16] border border-white/5 rounded-xl text-xs text-slate-300 leading-relaxed flex items-start space-x-2.5"
        >
          <span class="text-emerald-400 mt-0.5 font-bold">💡</span>
          <p>{{ rec }}</p>
        </div>
      </div>
    </div>
  </div>
</template>
