<script setup>
import { computed } from "vue"

const props = defineProps({
  consumoActual: Number,
  costoEstimado: Number,
  categoria: String
})

const badgeClass = computed(() => {
  const cat = props.categoria?.toUpperCase() || ""
  if (cat.includes("MODERADO")) return "bg-amber-500/10 border-amber-500/25 text-amber-400"
  if (cat.includes("INEFICIENTE") || cat.includes("ELEVADO") || cat.includes("ALTO")) return "bg-rose-500/10 border-rose-500/25 text-rose-400"
  return "bg-emerald-500/10 border-emerald-500/25 text-emerald-400"
})

const dotClass = computed(() => {
  const cat = props.categoria?.toUpperCase() || ""
  if (cat.includes("MODERADO")) return "bg-amber-400"
  if (cat.includes("INEFICIENTE") || cat.includes("ELEVADO") || cat.includes("ALTO")) return "bg-rose-400"
  return "bg-emerald-400"
})
</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-3 gap-4 sm:gap-6 items-stretch">
    <div class="min-h-[145px] bg-[#121824]/90 border border-white/5 shadow-xl p-5 sm:p-6 rounded-2xl flex flex-col justify-between relative overflow-hidden group hover:border-white/10 transition-all duration-300 backdrop-blur-xl">
      <svg class="absolute -bottom-4 -right-4 w-24 h-24 text-emerald-500/[0.04] transition-all duration-500 group-hover:text-emerald-500/[0.08] pointer-events-none" viewBox="0 0 24 24" fill="currentColor">
        <path d="M11.944 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12 12 12 0 0 0 12-12A12 12 0 0 0 12 0a12 12 0 0 0-.056 0zm4.962 7.224c.1-.002.321.023.465.14a.506.506 0 0 1 .171.325c.016.093.036.306.02.472-.18 1.898-.962 6.502-1.36 8.627-.168.9-.499 1.201-.82 1.23-.696.065-1.225-.46-1.9-.902-1.056-.693-1.653-1.124-2.678-1.8-1.185-.78-.417-1.21.258-1.91.177-.184 3.247-2.977 3.307-3.23.007-.032.014-.15-.056-.212s-.174-.041-.249-.024c-.106.024-1.793 1.14-5.061 3.345-.48.33-.913.49-1.302.48-.428-.008-1.252-.241-1.865-.44-.752-.245-1.349-.374-1.297-.789.027-.216.325-.437.893-.663 3.498-1.524 5.83-2.529 6.998-3.014 3.332-1.386 4.025-1.627 4.476-1.635z"/>
      </svg>
      <div class="flex items-start justify-between">
        <div>
          <span class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Consumo Actual</span>
          <p class="text-[11px] text-slate-500 mt-0.5 font-medium">Estimado del mes en curso</p>
        </div>
        <div class="w-9 h-9 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
          </svg>
        </div>
      </div>
      <div class="flex items-baseline space-x-1.5">
        <p class="text-[2.1rem] font-mono font-black text-white leading-none tracking-tight">
          {{ consumoActual }}
        </p>
        <span class="text-xs font-sans font-semibold text-slate-400">kWh / mes</span>
      </div>
    </div>

    <div class="min-h-[145px] bg-[#121824]/90 border border-white/5 shadow-xl p-5 sm:p-6 rounded-2xl flex flex-col justify-between relative overflow-hidden group hover:border-white/10 transition-all duration-300 backdrop-blur-xl">
      <svg class="absolute -bottom-4 -right-4 w-24 h-24 text-emerald-500/[0.04] transition-all duration-500 group-hover:text-emerald-500/[0.08] pointer-events-none" viewBox="0 0 24 24" fill="currentColor">
        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1.41 16.09V20h-2.67v-1.93c-1.71-.36-3.16-1.46-3.27-3.4h1.96c.1 1.05.82 1.87 2.65 1.87 1.96 0 2.4-.98 2.4-1.59 0-.83-.44-1.61-2.67-2.14-2.48-.6-4.18-1.62-4.18-3.67 0-1.72 1.39-2.84 3.11-3.21V4h2.67v1.95c1.86.45 2.79 1.86 2.85 3.39H14.3c-.05-1.11-.64-1.87-2.22-1.87-1.5 0-2.4.68-2.4 1.64 0 .84.65 1.39 2.67 1.91s4.18 1.39 4.18 3.91c-.01 1.83-1.38 2.83-3.12 3.16z"/>
      </svg>
      <div class="flex items-start justify-between">
        <div>
          <span class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Costo Estimado</span>
          <p class="text-[11px] text-slate-500 mt-0.5 font-medium">Proyección mensual en USD</p>
        </div>
        <div class="w-9 h-9 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="1" x2="12" y2="23"/>
            <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/>
          </svg>
        </div>
      </div>
      <div class="flex items-baseline">
        <p class="text-[2.1rem] font-mono font-black text-emerald-400 leading-none tracking-tight">
          $&thinsp;{{ Number(costoEstimado || 0).toFixed(2) }}
        </p>
      </div>
    </div>

    <div class="min-h-[145px] bg-[#121824]/90 border border-white/5 shadow-xl p-5 sm:p-6 rounded-2xl flex flex-col justify-between relative overflow-hidden group hover:border-white/10 transition-all duration-300 backdrop-blur-xl">
      <svg class="absolute -bottom-4 -right-4 w-24 h-24 text-emerald-500/[0.04] transition-all duration-500 group-hover:text-emerald-500/[0.08] pointer-events-none" viewBox="0 0 24 24" fill="currentColor">
        <path d="M12 2a10 10 0 1 0 10 10A10.011 10.011 0 0 0 12 2zm0 18a8 8 0 1 1 8-8 8.009 8.009 0 0 1-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z"/>
      </svg>
      <div class="flex items-start justify-between">
        <div>
          <span class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Estado del Sistema</span>
          <p class="text-[11px] text-slate-500 mt-0.5 font-medium">Motor de Inferencia IA</p>
        </div>
        <div class="w-9 h-9 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="3"/>
            <path d="M12 1v4M12 19v4M4.22 4.22l2.83 2.83M16.95 16.95l2.83 2.83M1 12h4M19 12h4M4.22 19.78l2.83-2.83M16.95 7.05l2.83-2.83"/>
          </svg>
        </div>
      </div>
      <div class="flex items-center space-x-2.5">
        <span :class="dotClass" class="w-2.5 h-2.5 rounded-full animate-pulse shrink-0"></span>
        <span :class="badgeClass" class="px-3 py-1 border font-black rounded-full text-xs uppercase tracking-wider shadow-sm">
          {{ categoria || "OPTIMIZADO" }}
        </span>
      </div>
    </div>
  </div>
</template>
