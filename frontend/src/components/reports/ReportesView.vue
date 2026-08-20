<script setup>
defineProps({
  historialReportes: { type: Array, default: () => [] },
  cargandoReportes: Boolean,
  generandoReporte: Boolean
})

const emit = defineEmits(['generar', 'eliminar'])

const getBadgeClass = (catStr) => {
  const cat = catStr?.toUpperCase() || ''
  if (cat.includes('MODERADO')) return 'bg-amber-500/10 border-amber-500/20 text-amber-400'
  if (cat.includes('INEFICIENTE') || cat.includes('ELEVADO') || cat.includes('ALTO')) return 'bg-rose-500/10 border-rose-500/20 text-rose-400'
  return 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
}
</script>

<template>
  <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h2 class="text-sm font-bold text-white uppercase tracking-wider">Historial de Reportes Energeticos</h2>
        <p class="text-xs text-slate-400 mt-0.5">Consulta los analisis e inferencias guardados en la base de datos.</p>
      </div>
      <button
        @click="emit('generar')"
        :disabled="generandoReporte"
        class="px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20 flex items-center space-x-1.5 disabled:opacity-50"
      >
        <span>⚡</span>
        <span>{{ generandoReporte ? 'Generando...' : 'Generar Nuevo Reporte' }}</span>
      </button>
    </div>

    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="border-b border-white/10 text-[10px] text-slate-400 uppercase tracking-widest">
            <th class="py-3 px-4 font-bold">ID / Fecha</th>
            <th class="py-3 px-4 font-bold">Consumo Total</th>
            <th class="py-3 px-4 font-bold">Costo Estimado</th>
            <th class="py-3 px-4 font-bold">Categoria IA</th>
            <th class="py-3 px-4 font-bold">Probabilidad</th>
            <th class="py-3 px-4 font-bold text-right">Acciones</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-white/5 text-xs">
          <tr v-for="rep in historialReportes" :key="rep.id" class="hover:bg-white/[0.02] transition">
            <td class="py-3.5 px-4 font-mono text-white">
              <div class="font-bold">#{{ rep.id }}</div>
              <div class="text-[10px] text-slate-400">{{ rep.fechaCreacion || rep.fecha_creacion || 'Reciente' }}</div>
            </td>
            <td class="py-3.5 px-4 text-slate-300 font-mono font-bold">{{ rep.consumoActual ?? rep.consumo_actual ?? 0 }} kWh</td>
            <td class="py-3.5 px-4 text-emerald-400 font-mono font-bold">$ {{ Number(rep.costoEstimado ?? rep.costo_estimado ?? 0).toFixed(2) }}</td>
            <td class="py-3.5 px-4">
              <span :class="getBadgeClass(rep.categoria)" class="px-2.5 py-1 rounded-full border font-bold text-[10px] uppercase tracking-wider">
                {{ rep.categoria || 'OPTIMIZADO' }}
              </span>
            </td>
            <td class="py-3.5 px-4 text-slate-300 font-mono">{{ rep.probabilidad != null ? Number(rep.probabilidad).toFixed(2) : 'N/A' }}</td>
            <td class="py-3.5 px-4 text-right">
              <button
                @click="emit('eliminar', rep)"
                class="px-3 py-1.5 bg-rose-500/10 hover:bg-rose-500/20 text-rose-400 border border-rose-500/20 rounded-xl transition font-bold text-[11px] hover:border-rose-500/40 inline-flex items-center"
              >
                Eliminar
              </button>
            </td>
          </tr>
          <tr v-if="historialReportes.length === 0 && !cargandoReportes">
            <td colspan="6" class="text-center py-12 text-slate-500 italic">No hay reportes previos. Haz clic en Generar Nuevo Reporte.</td>
          </tr>
          <tr v-if="cargandoReportes">
            <td colspan="6" class="text-center py-12 text-slate-400">Cargando historial...</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
