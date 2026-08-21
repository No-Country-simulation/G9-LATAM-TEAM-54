<script setup>
const emit = defineEmits(["go-login", "go-register"])
</script>

<template>
  <div class="flex-1 flex flex-col items-center justify-center text-center px-4 max-w-4xl mx-auto py-12 md:py-16 relative">
    <!-- Ambient background glow -->
    <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[500px] h-[350px] bg-emerald-500/10 rounded-full blur-[120px] pointer-events-none -z-10"></div>

    <!-- Badge with staggered animation delay 100 -->
    <div class="opacity-0 animate-fade-in-up delay-100 mb-6">
      <span class="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/25 text-emerald-400 text-xs font-bold uppercase tracking-widest shadow-sm">
        <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
        <span>Inteligencia Artificial para el Hogar</span>
      </span>
    </div>

    <!-- Main Title with staggered animation delay 200 -->
    <h1 class="opacity-0 animate-fade-in-up delay-200 text-4xl sm:text-5xl md:text-6xl font-black text-white tracking-tight leading-[1.15] mb-6 max-w-3xl">
      Optimiza tu consumo energético con <span class="text-transparent bg-clip-text bg-gradient-to-r from-emerald-400 via-teal-300 to-emerald-500">EnergiAI</span>
    </h1>

    <!-- Subtitle with staggered animation delay 300 -->
    <p class="opacity-0 animate-fade-in-up delay-300 text-slate-300 text-sm sm:text-base md:text-lg max-w-2xl mb-8 leading-relaxed font-normal">
      Analiza el rendimiento eléctrico de tus estancias, gestiona dispositivos por habitación y recibe recomendaciones inteligentes impulsadas por machine learning para reducir tu factura eléctrica.
    </p>

    <!-- CTA Action Buttons with staggered animation delay 400 -->
    <div class="opacity-0 animate-fade-in-up delay-400 flex flex-col sm:flex-row items-center justify-center gap-4 w-full sm:w-auto">
      <button
        @click="emit('go-register')"
        class="w-full sm:w-auto px-7 py-3.5 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-extrabold rounded-xl transition-all duration-200 shadow-xl shadow-emerald-500/25 hover:shadow-emerald-500/40 active:scale-[0.98] text-sm inline-flex items-center justify-center gap-2.5 cursor-pointer leading-none"
      >
        <svg class="w-4 h-4 text-slate-950 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
          <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
        </svg>
        <span>Comenzar Gratis</span>
      </button>
      <button
        @click="emit('go-login')"
        class="w-full sm:w-auto px-7 py-3.5 bg-[#121824]/90 hover:bg-white/5 border border-white/10 hover:border-emerald-500/30 text-slate-200 hover:text-white font-bold rounded-xl transition-all duration-200 text-sm backdrop-blur-md active:scale-[0.98] inline-flex items-center justify-center gap-2 cursor-pointer leading-none"
      >
        Acceder a mi cuenta
      </button>
    </div>

    <!-- Feature highlights with staggered animation delay 500 -->
    <div class="opacity-0 animate-fade-in-up delay-500 grid grid-cols-1 sm:grid-cols-3 gap-3 sm:gap-4 mt-12 pt-8 border-t border-white/5 max-w-2xl w-full">
      <div class="inline-flex items-center justify-center gap-2 px-3.5 py-2.5 rounded-xl bg-white/[0.02] border border-white/5 text-xs text-slate-400">
        <svg class="w-3.5 h-3.5 text-emerald-400 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="20 6 9 17 4 12"/>
        </svg>
        <span>Modelos ML Inferencia</span>
      </div>
      <div class="inline-flex items-center justify-center gap-2 px-3.5 py-2.5 rounded-xl bg-white/[0.02] border border-white/5 text-xs text-slate-400">
        <svg class="w-3.5 h-3.5 text-emerald-400 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="20 6 9 17 4 12"/>
        </svg>
        <span>Desglose por Estancias</span>
      </div>
      <div class="inline-flex items-center justify-center gap-2 px-3.5 py-2.5 rounded-xl bg-white/[0.02] border border-white/5 text-xs text-slate-400">
        <svg class="w-3.5 h-3.5 text-emerald-400 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="20 6 9 17 4 12"/>
        </svg>
        <span>Diagnósticos en Tiempo Real</span>
      </div>
    </div>
  </div>
</template>
