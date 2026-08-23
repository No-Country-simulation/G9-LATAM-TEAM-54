<script setup>
defineProps({
  estanciasDesglose: {
    type: Array,
    default: () => []
  },
  selectedEstanciaId: [Number, String]
})

const emit = defineEmits(["toggle-estancia", "abrir-modal-dispositivo"])
</script>

<template>
  <div class="h-[400px] bg-[#121824]/90 border border-white/5 rounded-2xl p-5 sm:p-6 shadow-xl backdrop-blur-xl flex flex-col justify-between overflow-hidden">
    <div class="flex justify-between items-center pb-3 shrink-0">
      <div>
        <h2 class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Estancias</h2>
        <p class="text-base font-extrabold text-white mt-0.5">Control por Habitaciones</p>
      </div>
      <div class="flex items-center space-x-2">
        <span class="px-2.5 py-1 rounded-lg bg-white/5 border border-white/10 text-slate-300 text-[10px] font-mono font-bold">
          {{ estanciasDesglose.length }} zonas
        </span>
        <button
          @click="emit('abrir-modal-dispositivo')"
          class="px-3 py-1.5 rounded-xl bg-emerald-500 hover:bg-emerald-400 text-slate-950 transition-all duration-200 inline-flex items-center justify-center gap-1 font-extrabold text-xs shadow-md shadow-emerald-500/20 active:scale-[0.98] cursor-pointer leading-none"
          title="Añadir Dispositivo"
        >
          <span>+</span>
          <span>Dispositivo</span>
        </button>
      </div>
    </div>

    <div class="flex-1 min-h-0 overflow-y-auto custom-scrollbar space-y-2.5 pr-1.5 mt-2">
      <div v-for="estancia in estanciasDesglose" :key="estancia.id" class="group">
        <div
          @click="emit('toggle-estancia', estancia)"
          :class="selectedEstanciaId === estancia.id ? 'border-emerald-500/40 bg-[#0d1320] shadow-md' : 'border-white/5 bg-[#090d16]/90 hover:border-white/10'"
          class="flex justify-between items-center px-4 py-3 rounded-xl border transition-all duration-200 cursor-pointer select-none"
        >
          <div>
            <div class="flex items-center space-x-2">
              <h4 class="font-bold text-white text-xs tracking-wide">{{ estancia.nombreEstancia }}</h4>
              <span v-if="estancia.dispositivos" class="text-[10px] text-emerald-400 font-mono font-semibold">
                ({{ estancia.dispositivos.length }} disp.)
              </span>
            </div>
            <p class="text-[11px] text-emerald-400/80 font-mono mt-0.5">{{ estancia.consumoKwh }} kWh total</p>
          </div>

          <div class="flex items-center space-x-3">
            <span class="font-mono font-bold text-slate-200 text-xs">$ {{ Number(estancia.costo || 0).toFixed(2) }}</span>
            <span
              class="text-slate-500 transition-transform duration-300"
              :class="{ 'rotate-180 text-emerald-400': selectedEstanciaId === estancia.id }"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
              </svg>
            </span>
          </div>
        </div>

        <div
          v-if="selectedEstanciaId === estancia.id"
          class="mt-1.5 pl-2 space-y-1.5"
        >
          <div v-if="estancia.dispositivos && estancia.dispositivos.length > 0">
            <div
              v-for="disp in estancia.dispositivos"
              :key="disp.id"
              class="flex justify-between items-center px-3.5 py-2.5 bg-[#0e1422] border border-white/5 rounded-xl text-xs"
            >
              <div class="flex items-center space-x-2.5">
                <div class="w-5 h-5 rounded-md bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400">
                  <svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M12 22v-5"/>
                    <path d="M9 8V2"/>
                    <path d="M15 8V2"/>
                    <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
                  </svg>
                </div>
                <div>
                  <p class="font-bold text-slate-200 text-xs">{{ disp.alias }}</p>
                  <p class="text-[10px] text-slate-500 font-mono">{{ disp.horasUsoDiarias }} hrs diarias</p>
                </div>
              </div>
              <span class="font-mono font-bold text-emerald-400 text-xs">{{ disp.consumoMensualKwh }} kWh</span>
            </div>
          </div>

          <div v-else class="px-3 py-3 text-center border border-dashed border-white/10 rounded-xl bg-white/[0.01]">
            <p class="text-slate-500 text-xs italic">Sin dispositivos registrados en esta estancia.</p>
          </div>
        </div>
      </div>

      <!-- Empty state with CTA -->
      <div v-if="estanciasDesglose.length === 0" class="h-full flex flex-col items-center justify-center text-center py-8 space-y-4">
        <div class="w-14 h-14 rounded-2xl bg-emerald-500/10 border border-emerald-500/25 flex items-center justify-center text-emerald-400 shadow-inner">
          <svg class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
          </svg>
        </div>
        <div>
          <p class="text-slate-200 font-bold text-sm">¡Bienvenido a EnergiAI!</p>
          <p class="text-slate-500 text-xs mt-1.5 max-w-[220px] leading-relaxed">Aún no tienes dispositivos registrados. Añade tu primer equipo para comenzar a monitorear el consumo por estancia.</p>
        </div>
        <button
          @click="emit('abrir-modal-dispositivo')"
          class="px-4 py-2.5 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-black rounded-xl text-xs transition-all duration-200 shadow-lg shadow-emerald-500/20 hover:shadow-emerald-500/35 active:scale-[0.98] inline-flex items-center gap-1.5 cursor-pointer leading-none"
        >
          <svg class="w-3.5 h-3.5 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          <span>Añadir Primer Dispositivo</span>
        </button>
      </div>
    </div>
  </div>
</template>
