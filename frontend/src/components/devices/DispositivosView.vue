<script setup>
import { computed } from "vue"

const props = defineProps({
  dispositivosRegistrados: {
    type: Array,
    default: () => []
  },
  cargandoDispositivos: Boolean
})

const emit = defineEmits(['abrir-modal', 'eliminar'])

const totalConsumo = computed(() =>
  props.dispositivosRegistrados.reduce((acc, d) => acc + (d.consumoMensualKwh ?? d.consumoKwh ?? 0), 0)
)

const totalEstancias = computed(() => {
  const set = new Set(props.dispositivosRegistrados.map(d => d.nombreEstancia || d.estanciaNombre || d.estanciaId))
  return set.size
})
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
      <div class="bg-[#121824]/90 border border-white/5 p-4 sm:p-5 rounded-2xl flex items-center justify-between shadow-xl backdrop-blur-xl">
        <div>
          <p class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Total Dispositivos</p>
          <p class="text-2xl font-mono font-black text-white mt-1">{{ dispositivosRegistrados.length }}</p>
        </div>
        <div class="w-10 h-10 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 22v-5"/>
            <path d="M9 8V2"/>
            <path d="M15 8V2"/>
            <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
          </svg>
        </div>
      </div>

      <div class="bg-[#121824]/90 border border-white/5 p-4 sm:p-5 rounded-2xl flex items-center justify-between shadow-xl backdrop-blur-xl">
        <div>
          <p class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Consumo Estimado Total</p>
          <p class="text-2xl font-mono font-black text-emerald-400 mt-1">{{ totalConsumo }} <span class="text-xs font-normal text-slate-400">kWh</span></p>
        </div>
        <div class="w-10 h-10 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
          </svg>
        </div>
      </div>

      <div class="bg-[#121824]/90 border border-white/5 p-4 sm:p-5 rounded-2xl flex items-center justify-between shadow-xl backdrop-blur-xl">
        <div>
          <p class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Estancias Asignadas</p>
          <p class="text-2xl font-mono font-black text-white mt-1">{{ totalEstancias }}</p>
        </div>
        <div class="w-10 h-10 rounded-xl bg-[#090d16] border border-white/5 flex items-center justify-center shrink-0 shadow-inner">
          <svg class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
          </svg>
        </div>
      </div>
    </div>

    <div class="bg-[#121824]/90 border border-white/5 rounded-2xl p-5 sm:p-6 shadow-xl backdrop-blur-xl flex flex-col justify-between overflow-hidden">
      <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center pb-5 gap-3 shrink-0 border-b border-white/5">
        <div>
          <h2 class="text-base font-extrabold text-white tracking-tight">Gestión de Dispositivos</h2>
          <p class="text-xs text-slate-400 mt-0.5">Administra las cargas eléctricas registradas en tu hogar u oficina</p>
        </div>
        <button
          @click="emit('abrir-modal')"
          class="px-4 py-2.5 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-black rounded-xl text-xs transition-all duration-200 shadow-lg shadow-emerald-500/20 hover:shadow-emerald-500/35 active:scale-[0.98] inline-flex items-center justify-center gap-1.5 cursor-pointer leading-none"
        >
          <svg class="w-3.5 h-3.5 text-slate-950 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="5" x2="12" y2="19"></line>
            <line x1="5" y1="12" x2="19" y2="12"></line>
          </svg>
          <span>Nuevo Dispositivo</span>
        </button>
      </div>

      <div class="overflow-x-auto overflow-y-auto max-h-[480px] custom-scrollbar mt-3">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr class="sticky top-0 bg-[#0d1320] border-b border-white/10 text-[10px] text-slate-400 uppercase tracking-widest z-10">
              <th class="py-3 px-4 font-extrabold">Alias / Dispositivo</th>
              <th class="py-3 px-4 font-extrabold">Estancia</th>
              <th class="py-3 px-4 font-extrabold">Variante / Capacidad</th>
              <th class="py-3 px-4 font-extrabold">Uso Diario</th>
              <th class="py-3 px-4 font-extrabold">Consumo Est.</th>
              <th class="py-3 px-4 font-extrabold text-right">Acciones</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-white/5 text-xs">
            <tr
              v-for="disp in dispositivosRegistrados"
              :key="disp.id || disp.dispositivoId"
              class="hover:bg-white/[0.02] transition-colors group"
            >
              <td class="py-3.5 px-4 font-bold text-white">
                <div class="flex items-center space-x-2.5">
                  <div class="w-7 h-7 rounded-lg bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 shrink-0">
                    <svg class="w-3.5 h-3.5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M12 22v-5"/>
                      <path d="M9 8V2"/>
                      <path d="M15 8V2"/>
                      <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
                    </svg>
                  </div>
                  <span class="text-slate-100 group-hover:text-emerald-400 transition-colors">{{ disp.alias }}</span>
                </div>
              </td>
              <td class="py-3.5 px-4 text-slate-300">
                <span class="px-2.5 py-1 rounded-lg bg-white/5 border border-white/10 text-[11px] font-medium text-slate-300">
                  {{ disp.nombreEstancia || disp.estanciaNombre || 'Estancia' }}
                </span>
              </td>
              <td class="py-3.5 px-4 text-slate-300 font-mono text-[11px]">
                {{ disp.nombreVariante || disp.varianteEtiqueta || 'Estándar' }}
                <span v-if="disp.potenciaWatts" class="text-emerald-400 text-[10px] font-bold ml-1">({{ disp.potenciaWatts }}W)</span>
              </td>
              <td class="py-3.5 px-4 text-slate-300 font-mono font-medium">{{ disp.horasUsoDiarias }} hrs / día</td>
              <td class="py-3.5 px-4 text-emerald-400 font-mono font-extrabold">{{ disp.consumoMensualKwh ?? disp.consumoKwh ?? 0 }} kWh</td>
              <td class="py-3.5 px-4 text-right">
                <button
                  @click="emit('eliminar', disp)"
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

            <tr v-if="dispositivosRegistrados.length === 0 && !cargandoDispositivos">
              <td colspan="6" class="text-center py-16 text-slate-500">
                <div class="flex flex-col items-center justify-center">
                  <div class="w-12 h-12 rounded-2xl bg-white/5 border border-white/5 flex items-center justify-center text-emerald-400 mb-3">
                    <svg class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M12 22v-5"/>
                      <path d="M9 8V2"/>
                      <path d="M15 8V2"/>
                      <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
                    </svg>
                  </div>
                  <p class="text-slate-400 font-bold text-sm">No hay dispositivos registrados</p>
                  <p class="text-slate-500 text-xs mt-1">Comienza agregando los equipos de tu hogar u oficina.</p>
                  <button
                    @click="emit('abrir-modal')"
                    class="mt-4 px-4 py-2 bg-emerald-500/10 hover:bg-emerald-500/20 border border-emerald-500/30 text-emerald-400 font-bold rounded-xl text-xs transition inline-flex items-center justify-center gap-1.5 leading-none"
                  >
                    <span>+</span>
                    <span>Registrar Primer Dispositivo</span>
                  </button>
                </div>
              </td>
            </tr>

            <tr v-if="cargandoDispositivos">
              <td colspan="6" class="text-center py-16 text-slate-400">
                <div class="flex items-center justify-center space-x-2">
                  <svg class="animate-spin h-5 w-5 text-emerald-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
                  </svg>
                  <span class="text-xs font-semibold">Cargando dispositivos registrados...</span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
