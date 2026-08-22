<script setup>
const emit = defineEmits(["go-login", "go-register"])
</script>

<template>
  <div class="flex-1 flex flex-col items-center justify-center text-center px-4 max-w-5xl mx-auto py-12 sm:py-16 md:py-20 relative select-none">
    
    <!-- Ambient Glow Effects (Multi-layered lighting) -->
    <div class="absolute top-1/4 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[350px] sm:w-[650px] md:w-[850px] h-[350px] sm:h-[450px] bg-gradient-to-tr from-emerald-500/15 via-teal-500/10 to-blue-600/10 rounded-full blur-[120px] sm:blur-[150px] pointer-events-none -z-10"></div>
    <div class="absolute top-2/3 left-1/4 -translate-x-1/2 w-[250px] sm:w-[400px] h-[250px] sm:h-[350px] bg-emerald-500/10 rounded-full blur-[100px] pointer-events-none -z-10"></div>
    <div class="absolute top-3/4 right-1/4 translate-x-1/2 w-[250px] sm:w-[400px] h-[250px] sm:h-[350px] bg-teal-500/10 rounded-full blur-[100px] pointer-events-none -z-10"></div>

    <!-- Badge Tag -->
    <div class="opacity-0 animate-fade-in-up delay-100 mb-6">
      <span class="inline-flex items-center space-x-2 px-4 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-bold uppercase tracking-widest shadow-lg shadow-emerald-500/10 backdrop-blur-md">
        <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
        <span>Inteligencia Artificial para el Hogar</span>
      </span>
    </div>

    <!-- Main Title -->
    <h1 class="opacity-0 animate-fade-in-up delay-200 text-3xl sm:text-5xl md:text-6xl font-black text-white tracking-tight leading-[1.15] mb-6 max-w-4xl">
      Optimiza tu consumo energético con <span class="text-transparent bg-clip-text bg-gradient-to-r from-emerald-400 via-teal-300 to-emerald-500">EnergiAI</span>
    </h1>

    <!-- Subtitle -->
    <p class="opacity-0 animate-fade-in-up delay-300 text-slate-300 text-sm sm:text-base md:text-lg max-w-2xl mb-9 leading-relaxed font-normal">
      Analiza el rendimiento eléctrico de tus estancias, gestiona dispositivos por habitación y recibe recomendaciones inteligentes impulsadas por machine learning para reducir tu factura eléctrica.
    </p>

    <!-- CTAs (Buttons) -->
    <div class="opacity-0 animate-fade-in-up delay-400 flex flex-col sm:flex-row items-center justify-center gap-3.5 sm:gap-4 w-full sm:w-auto">
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
        class="w-full sm:w-auto px-7 py-3.5 bg-[#151c2c]/90 hover:bg-[#1d273c] border border-white/20 hover:border-emerald-500/50 text-slate-100 hover:text-white font-extrabold rounded-xl transition-all duration-200 text-sm backdrop-blur-md shadow-xl shadow-black/40 hover:shadow-emerald-500/10 active:scale-[0.98] inline-flex items-center justify-center gap-2 cursor-pointer leading-none"
      >
        <svg class="w-4 h-4 text-emerald-400 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/>
          <polyline points="10 17 15 12 10 7"/>
          <line x1="15" y1="12" x2="3" y2="12"/>
        </svg>
        <span>Acceder a mi cuenta</span>
      </button>
    </div>

    <!-- Quick Pill Badges -->
    <div class="opacity-0 animate-fade-in-up delay-500 grid grid-cols-1 sm:grid-cols-3 gap-3 sm:gap-4 mt-12 pt-8 border-t border-white/5 max-w-3xl w-full">
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

    <!-- Feature Cards Section -->
    <div class="opacity-0 animate-fade-in-up delay-500 grid grid-cols-1 md:grid-cols-3 gap-5 sm:gap-6 mt-8 w-full text-left">
      <!-- Card 1 -->
      <div class="bg-[#121824]/85 border border-white/10 hover:border-emerald-500/40 p-6 rounded-2xl shadow-xl hover:shadow-2xl hover:shadow-emerald-500/10 transition-all duration-300 hover:-translate-y-1 backdrop-blur-xl group flex flex-col justify-between">
        <div>
          <div class="w-11 h-11 rounded-xl bg-emerald-500/10 border border-emerald-500/25 flex items-center justify-center text-emerald-400 mb-4 shadow-inner group-hover:scale-105 group-hover:border-emerald-500/50 transition-all duration-300">
            <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83"/>
              <circle cx="12" cy="12" r="4"/>
            </svg>
          </div>
          <h3 class="text-base font-extrabold text-white tracking-tight group-hover:text-emerald-400 transition-colors">
            Inferencia ML Predictiva
          </h3>
          <p class="text-xs text-slate-400 mt-2 leading-relaxed font-normal">
            Modelos de Machine Learning entrenados con ONNX que proyectan tu consumo mensual y estiman costos en función del clima y tus hábitos diarios.
          </p>
        </div>
        <div class="pt-4 mt-4 border-t border-white/5 flex items-center space-x-1.5 text-[11px] text-emerald-400 font-semibold">
          <span>&lt; 2% Margen de error</span>
          <svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </div>
      </div>

      <!-- Card 2 -->
      <div class="bg-[#121824]/85 border border-white/10 hover:border-emerald-500/40 p-6 rounded-2xl shadow-xl hover:shadow-2xl hover:shadow-emerald-500/10 transition-all duration-300 hover:-translate-y-1 backdrop-blur-xl group flex flex-col justify-between">
        <div>
          <div class="w-11 h-11 rounded-xl bg-emerald-500/10 border border-emerald-500/25 flex items-center justify-center text-emerald-400 mb-4 shadow-inner group-hover:scale-105 group-hover:border-emerald-500/50 transition-all duration-300">
            <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
          </div>
          <h3 class="text-base font-extrabold text-white tracking-tight group-hover:text-emerald-400 transition-colors">
            Control por Estancias
          </h3>
          <p class="text-xs text-slate-400 mt-2 leading-relaxed font-normal">
            Segmenta y monitorea las cargas eléctricas por habitación o zona de trabajo. Visualiza la distribución porcentual exacta de tu hogar.
          </p>
        </div>
        <div class="pt-4 mt-4 border-t border-white/5 flex items-center space-x-1.5 text-[11px] text-emerald-400 font-semibold">
          <span>Desglose por habitación</span>
          <svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </div>
      </div>

      <!-- Card 3 -->
      <div class="bg-[#121824]/85 border border-white/10 hover:border-emerald-500/40 p-6 rounded-2xl shadow-xl hover:shadow-2xl hover:shadow-emerald-500/10 transition-all duration-300 hover:-translate-y-1 backdrop-blur-xl group flex flex-col justify-between">
        <div>
          <div class="w-11 h-11 rounded-xl bg-emerald-500/10 border border-emerald-500/25 flex items-center justify-center text-emerald-400 mb-4 shadow-inner group-hover:scale-105 group-hover:border-emerald-500/50 transition-all duration-300">
            <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              <polygon points="12 8 10 13 14 13 12 18"/>
            </svg>
          </div>
          <h3 class="text-base font-extrabold text-white tracking-tight group-hover:text-emerald-400 transition-colors">
            Eficiencia en Tiempo Real
          </h3>
          <p class="text-xs text-slate-400 mt-2 leading-relaxed font-normal">
            Diagnósticos continuos del estado energético y recomendaciones personalizadas inteligentes para reducir el costo de tu factura.
          </p>
        </div>
        <div class="pt-4 mt-4 border-t border-white/5 flex items-center space-x-1.5 text-[11px] text-emerald-400 font-semibold">
          <span>Ahorro garantizado</span>
          <svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </div>
      </div>
    </div>

  </div>
</template>

