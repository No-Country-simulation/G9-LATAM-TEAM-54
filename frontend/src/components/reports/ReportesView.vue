<script setup>
import { computed } from "vue"

const props = defineProps({
  historialReportes: { type: Array, default: () => [] },
  cargandoReportes: Boolean,
  generandoReporte: Boolean
})

const emit = defineEmits(['generar', 'eliminar'])

const getBadgeClass = (catStr) => {
  const cat = catStr?.toUpperCase() || ''
  if (cat.includes('MODERADO')) return 'bg-amber-500/10 border-amber-500/25 text-amber-400'
  if (cat.includes('INEFICIENTE') || cat.includes('ELEVADO') || cat.includes('ALTO')) return 'bg-rose-500/10 border-rose-500/25 text-rose-400'
  return 'bg-emerald-500/10 border-emerald-500/25 text-emerald-400'
}

const getDotClass = (catStr) => {
  const cat = catStr?.toUpperCase() || ''
  if (cat.includes('MODERADO')) return 'bg-amber-400'
  if (cat.includes('INEFICIENTE') || cat.includes('ELEVADO') || cat.includes('ALTO')) return 'bg-rose-400'
  return 'bg-emerald-400'
}

const formatFecha = (str) => {
  if (!str) return "Reciente"
  try {
    const d = new Date(str)
    if (isNaN(d.getTime())) return str
    return d.toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit"
    })
  } catch {
    return str
  }
}

const totalConsumoPromedio = computed(() => {
  if (props.historialReportes.length === 0) return 0
  const sum = props.historialReportes.reduce((acc, r) => acc + (r.consumoActual ?? r.consumo_actual ?? 0), 0)
  return (sum / props.historialReportes.length).toFixed(1)
})

const ultimoDiagnostico = computed(() => {
  if (props.historialReportes.length === 0) return "N/A"
  return props.historialReportes[0]?.categoria || "OPTIMIZADO"
})
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
      <div class="bg-[#121824]/90 border border-white/5 p-4 sm:p-5 rounded-2xl flex items-center justify-between shadow-xl backdrop-blur-xl">
        <div>
          <p class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Total Reportes</p>
          <p class="text-2xl font-mono font-black text-white mt-1">{{ historialReportes.length }}</p>
        </div>
        <div class="w-10 h-10 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/>
            <line x1="16" y1="13" x2="8" y2="13"/>
            <line x1="16" y1="17" x2="8" y2="17"/>
          </svg>
        </div>
      </div>

      <div class="bg-[#121824]/90 border border-white/5 p-4 sm:p-5 rounded-2xl flex items-center justify-between shadow-xl backdrop-blur-xl">
        <div>
          <p class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Último Diagnóstico</p>
          <p class="text-lg font-mono font-black text-emerald-400 mt-1 uppercase">{{ ultimoDiagnostico }}</p>
        </div>
        <div class="w-10 h-10 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
          </svg>
        </div>
      </div>

      <div class="bg-[#121824]/90 border border-white/5 p-4 sm:p-5 rounded-2xl flex items-center justify-between shadow-xl backdrop-blur-xl">
        <div>
          <p class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Consumo Promedio</p>
          <p class="text-2xl font-mono font-black text-white mt-1">{{ totalConsumoPromedio }} <span class="text-xs font-normal text-slate-400">kWh</span></p>
        </div>
        <div class="w-10 h-10 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="20" x2="18" y2="10"/>
            <line x1="12" y1="20" x2="12" y2="4"/>
            <line x1="6" y1="20" x2="6" y2="14"/>
          </svg>
        </div>
      </div>
    </div>

    <div class="bg-[#121824]/90 border border-white/5 rounded-2xl p-5 sm:p-6 shadow-xl backdrop-blur-xl flex flex-col justify-between overflow-hidden">
      <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center pb-5 gap-3 shrink-0 border-b border-white/5">
        <div>
          <h2 class="text-base font-extrabold text-white tracking-tight">Historial de Reportes Energéticos</h2>
          <p class="text-xs text-slate-400 mt-0.5">Consulta los análisis e inferencias guardados en la base de datos</p>
        </div>
        <button
          @click="emit('generar')"
          :disabled="generandoReporte"
          class="px-4 py-2.5 bg-emerald-500 hover:bg-emerald-400 disabled:bg-emerald-500/50 text-slate-950 font-black rounded-xl text-xs transition-all duration-200 shadow-lg shadow-emerald-500/20 hover:shadow-emerald-500/35 active:scale-[0.98] inline-flex items-center justify-center gap-2 cursor-pointer disabled:cursor-not-allowed leading-none"
        >
          <svg v-if="!generandoReporte" class="w-3.5 h-3.5 text-slate-950 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
          </svg>
          <svg v-else class="animate-spin h-3.5 w-3.5 text-slate-950 shrink-0" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
          </svg>
          <span>{{ generandoReporte ? 'Generando Inferencia...' : 'Generar Nuevo Reporte' }}</span>
        </button>
      </div>

      <div class="overflow-x-auto max-h-[480px] custom-scrollbar mt-3 -mx-2 sm:mx-0 px-2 sm:px-0">
        <table class="w-full min-w-[700px] text-left border-collapse">
          <thead>
            <tr class="sticky top-0 bg-[#0d1320] border-b border-white/10 text-[10px] text-slate-400 uppercase tracking-widest z-10">
              <th class="py-3 px-4 font-extrabold whitespace-nowrap">ID / Fecha</th>
              <th class="py-3 px-4 font-extrabold whitespace-nowrap">Consumo Total</th>
              <th class="py-3 px-4 font-extrabold whitespace-nowrap">Costo Estimado</th>
              <th class="py-3 px-4 font-extrabold whitespace-nowrap">Categoría IA</th>
              <th class="py-3 px-4 font-extrabold whitespace-nowrap">Confianza / Prob.</th>
              <th class="py-3 px-4 font-extrabold text-right whitespace-nowrap">Acciones</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-white/5 text-xs">
            <tr
              v-for="rep in historialReportes"
              :key="rep.id"
              class="hover:bg-white/[0.02] transition-colors group"
            >
              <td class="py-3.5 px-4 font-mono text-white whitespace-nowrap">
                <div class="font-black text-xs text-white group-hover:text-emerald-400 transition-colors">#{{ rep.id }}</div>
                <div class="text-[10px] text-slate-400 mt-0.5">{{ formatFecha(rep.fechaCreacion || rep.fecha_creacion) }}</div>
              </td>
              <td class="py-3.5 px-4 text-slate-100 font-mono font-bold whitespace-nowrap">{{ rep.consumoActual ?? rep.consumo_actual ?? 0 }} kWh</td>
              <td class="py-3.5 px-4 text-emerald-400 font-mono font-black whitespace-nowrap">$ {{ Number(rep.costoEstimado ?? rep.costo_estimado ?? 0).toFixed(2) }}</td>
              <td class="py-3.5 px-4 whitespace-nowrap">
                <span :class="getBadgeClass(rep.categoria)" class="inline-flex items-center space-x-1.5 px-2.5 py-1 rounded-full border font-black text-[10px] uppercase tracking-wider shadow-sm">
                  <span :class="getDotClass(rep.categoria)" class="w-1.5 h-1.5 rounded-full"></span>
                  <span>{{ rep.categoria || 'OPTIMIZADO' }}</span>
                </span>
              </td>
              <td class="py-3.5 px-4 text-slate-300 font-mono font-medium whitespace-nowrap">
                <div class="flex items-center space-x-2">
                  <span>{{ rep.probabilidad != null ? (Number(rep.probabilidad) * 100).toFixed(0) + '%' : '100%' }}</span>
                  <div class="w-16 bg-white/10 rounded-full h-1.5 overflow-hidden hidden sm:block">
                    <div
                      class="bg-emerald-400 h-full rounded-full"
                      :style="{ width: (rep.probabilidad != null ? (Number(rep.probabilidad) * 100) : 100) + '%' }"
                    ></div>
                  </div>
                </div>
              </td>
              <td class="py-3.5 px-4 text-right whitespace-nowrap">
                <button
                  @click="emit('eliminar', rep)"
                  class="px-3 py-1.5 bg-rose-500/10 hover:bg-rose-500/20 text-rose-400 border border-rose-500/25 rounded-xl transition-all duration-200 font-bold text-[11px] hover:border-rose-500/40 inline-flex items-center justify-center gap-1.5 cursor-pointer active:scale-[0.98] leading-none"
                >
                  <svg class="w-3.5 h-3.5 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                    <line x1="10" y1="11" x2="10" y2="17"></line>
                    <line x1="14" y1="11" x2="14" y2="17"></line>
                  </svg>
                  <span>Eliminar</span>
                </button>
              </td>
            </tr>

            <tr v-if="historialReportes.length === 0 && !cargandoReportes">
              <td colspan="6" class="text-center py-16 text-slate-500">
                <div class="flex flex-col items-center justify-center">
                  <div class="w-12 h-12 rounded-2xl bg-white/5 border border-white/5 flex items-center justify-center text-emerald-400 mb-3">
                    <svg class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                      <polyline points="14 2 14 8 20 8"/>
                      <line x1="16" y1="13" x2="8" y2="13"/>
                      <line x1="16" y1="17" x2="8" y2="17"/>
                    </svg>
                  </div>
                  <p class="text-slate-400 font-bold text-sm">No hay reportes registrados aún</p>
                  <p class="text-slate-500 text-xs mt-1">Genera un análisis con el motor de IA para predecir tu consumo.</p>
                  <button
                    @click="emit('generar')"
                    :disabled="generandoReporte"
                    class="mt-4 px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20 inline-flex items-center justify-center gap-1.5 leading-none"
                  >
                    <svg class="w-3.5 h-3.5 text-slate-950 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                      <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
                    </svg>
                    <span>Generar Primer Reporte</span>
                  </button>
                </div>
              </td>
            </tr>

            <tr v-if="cargandoReportes">
              <td colspan="6" class="text-center py-16 text-slate-400">
                <div class="flex items-center justify-center space-x-2">
                  <svg class="animate-spin h-5 w-5 text-emerald-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
                  </svg>
                  <span class="text-xs font-semibold">Cargando historial de reportes...</span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
