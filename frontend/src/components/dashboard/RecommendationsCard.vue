<script setup>
defineProps({
  recomendaciones: {
    type: Array,
    default: () => []
  }
})
</script>

<template>
  <div class="h-[400px] bg-[#121824]/90 border border-white/5 rounded-2xl p-5 sm:p-6 shadow-xl backdrop-blur-xl flex flex-col justify-between overflow-hidden">
    <div class="flex items-center justify-between pb-3 shrink-0">
      <div>
        <h2 class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Recomendaciones IA</h2>
        <p class="text-base font-extrabold text-white mt-0.5">Optimización Activa</p>
      </div>
      <span class="inline-flex items-center space-x-1.5 px-2.5 py-1 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-[10px] font-extrabold uppercase tracking-widest">
        <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
        <span>Motor Activo</span>
      </span>
    </div>

    <div class="flex-1 min-h-0 overflow-y-auto custom-scrollbar space-y-2.5 pr-1.5 mt-2">
      <div
        v-for="(rec, index) in recomendaciones"
        :key="index"
        class="p-3.5 bg-[#090d16]/90 border border-white/5 hover:border-emerald-500/20 transition-all rounded-xl text-xs text-slate-300 leading-relaxed flex items-start space-x-2.5 group"
      >
        <div class="w-5 h-5 rounded-md bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 shrink-0 mt-0.5 shadow-sm">
          <svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18h6"/>
            <path d="M10 22h4"/>
            <path d="M12 2a7 7 0 0 0-7 7c0 2.5 1.5 4.5 3.5 5.5.5.5.5 1 .5 1.5h6c0-.5 0-1 .5-1.5 2-1 3.5-3 3.5-5.5a7 7 0 0 0-7-7z"/>
          </svg>
        </div>
        <p class="text-slate-300 group-hover:text-slate-100 transition-colors text-[12px] font-medium leading-relaxed">{{ rec }}</p>
      </div>

      <!-- Empty state: no recomendaciones aún -->
      <div v-if="recomendaciones.length === 0" class="h-full flex flex-col items-center justify-center py-6 text-center space-y-3">
        <div class="w-12 h-12 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 shadow-inner">
          <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18h6"/>
            <path d="M10 22h4"/>
            <path d="M12 2a7 7 0 0 0-7 7c0 2.5 1.5 4.5 3.5 5.5.5.5.5 1 .5 1.5h6c0-.5 0-1 .5-1.5 2-1 3.5-3 3.5-5.5a7 7 0 0 0-7-7z"/>
          </svg>
        </div>
        <div>
          <p class="text-slate-300 font-bold text-sm">Motor IA en espera</p>
          <p class="text-slate-500 text-xs mt-1.5 max-w-[200px] leading-relaxed">Registra al menos un dispositivo y genera un reporte para activar las sugerencias de optimización personalizadas.</p>
        </div>
      </div>
    </div>

    <div class="pt-3 mt-2 border-t border-white/5 shrink-0 flex items-center justify-between text-[11px] text-slate-500 font-medium">
      <span>Inferencias basadas en ONNX</span>
      <span class="text-emerald-400 font-mono font-semibold">100% Precisión</span>
    </div>
  </div>
</template>
