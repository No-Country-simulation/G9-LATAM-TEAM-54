<script setup>
const emit = defineEmits(["go-login", "go-register"])
</script>

<template>
  <div class="flex-1 flex flex-col items-center justify-center text-center px-4 max-w-4xl mx-auto py-12">
    <span class="px-3 py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-bold uppercase tracking-widest mb-6">
      Inteligencia Artificial para el Hogar
    </span>
    <h1 class="text-4xl md:text-6xl font-black text-white tracking-tight leading-tight mb-6">
      Optimiza tu consumo energético con <span class="text-emerald-400">EnergiAI</span>
    </h1>
    <p class="text-slate-400 text-sm md:text-base max-w-2xl mb-8 leading-relaxed">
      Analiza el rendimiento eléctrico de tus estancias, gestiona dispositivos por habitación y recibe recomendaciones inteligentes impulsadas por machine learning para reducir tu factura eléctrica.
    </p>
    <div class="flex space-x-4">
      <button
        @click="emit('go-register')"
        class="px-6 py-3.5 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-950 font-bold rounded-xl transition shadow-xl shadow-emerald-500/20 text-sm"
      >
        Comenzar Gratis
      </button>
      <button
        @click="emit('go-login')"
        class="px-6 py-3.5 bg-[#121824] hover:bg-white/5 border border-white/10 text-white font-bold rounded-xl transition text-sm"
      >
        Acceder a mi cuenta
      </button>
    </div>
  </div>
</template>
