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
  <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col">
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest">Estancias</h2>
      <div class="flex items-center space-x-2">
        <span class="text-[11px] text-slate-400 font-medium">{{ estanciasDesglose.length }} reg.</span>
        <button
          @click="emit('abrir-modal-dispositivo')"
          class="px-2.5 py-1 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 hover:bg-emerald-500 hover:text-slate-950 transition flex items-center space-x-1 font-bold text-xs"
          title="Añadir Dispositivo"
        >
          <span>+ Dispositivo</span>
        </button>
      </div>
    </div>

    <div class="space-y-4 max-h-[500px] overflow-y-auto pr-1">
      <div v-for="estancia in estanciasDesglose" :key="estancia.id" class="group">
        <div
          @click="emit('toggle-estancia', estancia)"
          :class="selectedEstanciaId === estancia.id ? 'border-emerald-500/50 bg-[#0d1320]' : 'border-white/5 bg-[#090d16] hover:border-white/10'"
          class="flex justify-between items-center px-5 py-4 rounded-xl border transition-all duration-300 cursor-pointer select-none"
        >
          <div>
            <div class="flex items-center space-x-2">
              <h4 class="font-bold text-white text-sm tracking-wide">{{ estancia.nombreEstancia }}</h4>
              <span v-if="estancia.dispositivos" class="text-[10px] text-emerald-400 font-mono">({{ estancia.dispositivos.length }} disp.)</span>
            </div>
            <p class="text-[11px] text-emerald-400/80 font-mono mt-0.5">{{ estancia.consumoKwh }} kWh total</p>
          </div>

          <div class="flex items-center space-x-4">
            <span class="font-mono font-bold text-white text-sm">$ {{ Number(estancia.costo || 0).toFixed(2) }}</span>
            <span
              class="text-slate-500 transition-transform duration-300"
              :class="{ 'rotate-180 text-emerald-400': selectedEstanciaId === estancia.id }"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
              </svg>
            </span>
          </div>
        </div>

        <div
          v-if="selectedEstanciaId === estancia.id"
          class="mt-2 pl-2 space-y-2 animate-in fade-in slide-in-from-top-2 duration-300"
        >
          <div v-if="estancia.dispositivos && estancia.dispositivos.length > 0">
            <div
              v-for="disp in estancia.dispositivos"
              :key="disp.id"
              class="flex justify-between items-center px-4 py-3 bg-[#0f1420] border border-white/5 rounded-xl text-sm"
            >
              <div class="flex items-center space-x-3">
                <span class="text-emerald-500">🔌</span>
                <div>
                  <p class="font-bold text-slate-200 text-xs">{{ disp.alias }}</p>
                  <p class="text-[10px] text-slate-500 font-mono">{{ disp.horasUsoDiarias }} horas diarias</p>
                </div>
              </div>
              <span class="font-mono font-bold text-slate-300 text-xs">{{ disp.consumoMensualKwh }} kWh</span>
            </div>
          </div>

          <div v-else class="px-4 py-4 text-center border border-dashed border-white/10 rounded-xl">
            <p class="text-slate-500 text-xs italic">Sin dispositivos registrados en esta estancia.</p>
          </div>
        </div>
      </div>

      <p v-if="estanciasDesglose.length === 0" class="text-slate-500 text-xs text-center py-6">Sin registros.</p>
    </div>
  </div>
</template>
