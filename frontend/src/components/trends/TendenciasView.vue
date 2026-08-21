<script setup>
</script>

<template>
  <div class="space-y-6">
    <div class="bg-[#121824]/90 border border-white/5 rounded-2xl p-8 sm:p-12 shadow-xl backdrop-blur-xl text-center flex flex-col items-center justify-center">
      <div class="w-16 h-16 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 mb-4 shadow-inner">
        <svg class="w-8 h-8" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
          <polyline points="17 6 23 6 23 12"/>
        </svg>
      </div>
      <h2 class="text-xl font-extrabold text-white tracking-tight">Tendencias y Modelado Predictivo</h2>
      <p class="text-xs text-slate-400 max-w-md mt-2 leading-relaxed font-medium">
        Módulo para análisis histórico detallado, estacionalidad climática y proyecciones de consumo eléctrico mediante Machine Learning.
      </p>

      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mt-8 w-full max-w-2xl">
        <div class="p-4 bg-[#090d16]/80 border border-white/5 rounded-xl text-left">
          <p class="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Histórico Anual</p>
          <p class="text-sm font-bold text-white mt-1">Estacionalidad</p>
          <p class="text-[11px] text-slate-500 mt-0.5">Patrones térmicos</p>
        </div>
        <div class="p-4 bg-[#090d16]/80 border border-white/5 rounded-xl text-left">
          <p class="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Proyecciones</p>
          <p class="text-sm font-bold text-emerald-400 mt-1">Modelo ONNX</p>
          <p class="text-[11px] text-slate-500 mt-0.5">Margen de error &lt; 2%</p>
        </div>
        <div class="p-4 bg-[#090d16]/80 border border-white/5 rounded-xl text-left">
          <p class="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Ahorro Estimado</p>
          <p class="text-sm font-bold text-white mt-1">Simulación</p>
          <p class="text-[11px] text-slate-500 mt-0.5">Optimización horaria</p>
        </div>
      </div>
    </div>
  </div>
</template>
