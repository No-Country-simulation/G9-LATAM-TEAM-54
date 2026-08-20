<script setup>
defineProps({
  dispositivosRegistrados: {
    type: Array,
    default: () => []
  },
  cargandoDispositivos: Boolean
})

const emit = defineEmits(['abrir-modal', 'eliminar'])
</script>

<template>
  <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h2 class="text-sm font-bold text-white uppercase tracking-wider">Gestion de Dispositivos</h2>
        <p class="text-xs text-slate-400 mt-0.5">Administra las cargas electricas registradas en tu hogar u oficina.</p>
      </div>
      <button
        @click="emit('abrir-modal')"
        class="px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20"
      >
        + Nuevo Dispositivo
      </button>
    </div>

    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="border-b border-white/10 text-[10px] text-slate-400 uppercase tracking-widest">
            <th class="py-3 px-4 font-bold">Alias / Dispositivo</th>
            <th class="py-3 px-4 font-bold">Estancia</th>
            <th class="py-3 px-4 font-bold">Variante / Capacidad</th>
            <th class="py-3 px-4 font-bold">Uso Diario</th>
            <th class="py-3 px-4 font-bold">Consumo Est.</th>
            <th class="py-3 px-4 font-bold text-right">Acciones</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-white/5 text-xs">
          <tr v-for="disp in dispositivosRegistrados" :key="disp.id || disp.dispositivoId" class="hover:bg-white/[0.02] transition">
            <td class="py-3.5 px-4 font-bold text-white">
              <div class="flex items-center space-x-2">
                <span class="text-emerald-400">🔌</span>
                <span>{{ disp.alias }}</span>
              </div>
            </td>
            <td class="py-3.5 px-4 text-slate-300">
              <span class="px-2.5 py-1 rounded-full bg-white/5 border border-white/10 text-[11px]">
                {{ disp.nombreEstancia || disp.estanciaNombre || 'Estancia' }}
              </span>
            </td>
            <td class="py-3.5 px-4 text-slate-400 font-mono">
              {{ disp.nombreVariante || disp.varianteEtiqueta || 'Estandar' }}
              <span v-if="disp.potenciaWatts" class="text-emerald-400 text-[10px] ml-1">({{ disp.potenciaWatts }}W)</span>
            </td>
            <td class="py-3.5 px-4 text-slate-300 font-mono">{{ disp.horasUsoDiarias }} hrs/dia</td>
            <td class="py-3.5 px-4 text-emerald-400 font-mono font-bold">{{ disp.consumoMensualKwh ?? disp.consumoKwh ?? 0 }} kWh</td>
            <td class="py-3.5 px-4 text-right">
              <button
                @click="emit('eliminar', disp)"
                class="px-3 py-1.5 bg-rose-500/10 hover:bg-rose-500/20 text-rose-400 border border-rose-500/20 rounded-xl transition font-bold text-[11px]"
              >
                Eliminar
              </button>
            </td>
          </tr>
          <tr v-if="dispositivosRegistrados.length === 0 && !cargandoDispositivos">
            <td colspan="6" class="text-center py-12 text-slate-500 italic">No hay dispositivos registrados.</td>
          </tr>
          <tr v-if="cargandoDispositivos">
            <td colspan="6" class="text-center py-12 text-slate-400">Cargando dispositivos...</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
