<script setup>
import { computed } from "vue"
import { Doughnut } from "vue-chartjs"

const COLORS = ["#10b981", "#06b6d4", "#3b82f6", "#f59e0b", "#8b5cf6", "#ec4899"]

const props = defineProps({
  estanciasDesglose: {
    type: Array,
    default: () => []
  }
})

const totalKwh = computed(() =>
  props.estanciasDesglose.reduce((sum, e) => sum + (e.consumoKwh || 0), 0)
)

const doughnutChartData = computed(() => ({
  labels: props.estanciasDesglose.map(e => e.nombreEstancia),
  datasets: [
    {
      backgroundColor: COLORS,
      hoverBackgroundColor: COLORS.map(c => c + "dd"),
      data: props.estanciasDesglose.map(e => e.consumoKwh),
      borderWidth: 2,
      borderColor: "#121824",
      hoverOffset: 6,
    }
  ]
}))

const doughnutChartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  cutout: "72%",
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: "#0c121e",
      borderColor: "rgba(255,255,255,0.1)",
      borderWidth: 1,
      titleColor: "#f8fafc",
      bodyColor: "#94a3b8",
      padding: 10,
      callbacks: {
        label: (ctx) => {
          const val = ctx.parsed
          const pct = totalKwh.value > 0 ? ((val / totalKwh.value) * 100).toFixed(1) : 0
          return ` ${val} kWh  (${pct}%)`
        }
      }
    }
  }
}))

const pct = (kwh) =>
  totalKwh.value > 0 ? ((kwh / totalKwh.value) * 100).toFixed(1) : "0.0"
</script>

<template>
  <div class="h-[390px] bg-[#121824]/90 border border-white/5 rounded-2xl p-5 sm:p-6 shadow-xl backdrop-blur-xl flex flex-col justify-between overflow-hidden">
    <!-- Header -->
    <div class="flex items-center justify-between pb-2 shrink-0">
      <div>
        <h2 class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">Distribución</h2>
        <p class="text-base font-extrabold text-white mt-0.5">Por Estancia</p>
      </div>
      <span class="px-2.5 py-1 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-[10px] font-extrabold uppercase tracking-widest">
        {{ estanciasDesglose.length }} zonas
      </span>
    </div>

    <!-- Body content with fixed height and internal scroll -->
    <div v-if="estanciasDesglose.length > 0" class="flex-1 min-h-0 flex flex-col justify-between pt-1">
      <!-- Donut Chart Canvas -->
      <div class="relative mx-auto shrink-0 my-1" style="width: 140px; height: 140px;">
        <Doughnut :data="doughnutChartData" :options="doughnutChartOptions" />
        <div class="absolute inset-0 flex flex-col items-center justify-center pointer-events-none select-none">
          <span class="text-[9px] text-slate-400 font-bold uppercase tracking-wider leading-none">Total</span>
          <span class="text-lg font-black text-white font-mono leading-tight mt-0.5">{{ totalKwh }}</span>
          <span class="text-[9px] text-emerald-400 font-semibold leading-none">kWh</span>
        </div>
      </div>

      <!-- Scrollable Room Breakdown List -->
      <div class="flex-1 min-h-0 overflow-y-auto custom-scrollbar space-y-1.5 pr-1 mt-2">
        <div
          v-for="(estancia, i) in estanciasDesglose"
          :key="estancia.id"
          class="flex items-center justify-between px-3 py-2 rounded-xl bg-[#090d16]/90 border border-white/5 hover:border-white/10 transition group"
        >
          <div class="flex items-center space-x-2.5 min-w-0">
            <span
              class="w-2.5 h-2.5 rounded-full shrink-0 shadow-sm"
              :style="{ backgroundColor: COLORS[i % COLORS.length] }"
            ></span>
            <span class="text-xs text-slate-200 font-medium truncate group-hover:text-white transition">
              {{ estancia.nombreEstancia }}
            </span>
          </div>
          <div class="flex items-center space-x-2 shrink-0 ml-2">
            <span class="text-[11px] text-slate-400 font-mono font-medium">{{ estancia.consumoKwh }} kWh</span>
            <span
              class="text-[10px] font-bold px-2 py-0.5 rounded-full font-mono"
              :style="{
                backgroundColor: COLORS[i % COLORS.length] + '18',
                color: COLORS[i % COLORS.length]
              }"
            >
              {{ pct(estancia.consumoKwh) }}%
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="flex-1 flex flex-col items-center justify-center text-center p-4">
      <div class="w-12 h-12 rounded-2xl bg-white/5 border border-white/5 flex items-center justify-center text-xl mb-2">📊</div>
      <p class="text-slate-400 text-xs font-bold">Sin datos de estancias</p>
      <p class="text-slate-500 text-[11px] mt-0.5">Registra dispositivos para ver la distribución</p>
    </div>
  </div>
</template>
