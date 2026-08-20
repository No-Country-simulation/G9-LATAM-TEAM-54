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
  <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col">
    <div class="flex items-center justify-between mb-5">
      <div>
        <h2 class="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Distribucion</h2>
        <p class="text-sm font-extrabold text-white mt-0.5">Por Estancia</p>
      </div>
      <span class="px-2.5 py-1 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-[10px] font-bold uppercase tracking-widest">
        {{ estanciasDesglose.length }} zonas
      </span>
    </div>

    <div v-if="estanciasDesglose.length > 0" class="flex flex-col gap-5">
      <div class="relative mx-auto" style="width: 160px; height: 160px;">
        <Doughnut :data="doughnutChartData" :options="doughnutChartOptions" />
        <div class="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
          <span class="text-[10px] text-slate-500 font-semibold uppercase tracking-wider leading-none">Total</span>
          <span class="text-xl font-black text-white font-mono leading-tight mt-0.5">{{ totalKwh }}</span>
          <span class="text-[10px] text-slate-400 font-medium leading-none">kWh</span>
        </div>
      </div>

      <div class="space-y-2">
        <div
          v-for="(estancia, i) in estanciasDesglose"
          :key="estancia.id"
          class="flex items-center justify-between px-3 py-2 rounded-xl bg-[#090d16] border border-white/5 hover:border-white/10 transition group"
        >
          <div class="flex items-center space-x-2.5 min-w-0">
            <span
              class="w-2.5 h-2.5 rounded-full shrink-0"
              :style="{ backgroundColor: COLORS[i % COLORS.length] }"
            ></span>
            <span class="text-[12px] text-slate-300 font-medium truncate group-hover:text-white transition">
              {{ estancia.nombreEstancia }}
            </span>
          </div>
          <div class="flex items-center space-x-3 shrink-0 ml-2">
            <span class="text-[11px] text-slate-400 font-mono">{{ estancia.consumoKwh }} kWh</span>
            <span
              class="text-[10px] font-bold px-2 py-0.5 rounded-full"
              :style="{
                backgroundColor: COLORS[i % COLORS.length] + '1a',
                color: COLORS[i % COLORS.length]
              }"
            >
              {{ pct(estancia.consumoKwh) }}%
            </span>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="flex-1 flex flex-col items-center justify-center py-10 text-center">
      <div class="w-16 h-16 rounded-2xl bg-white/5 border border-white/5 flex items-center justify-center text-2xl mb-3">📊</div>
      <p class="text-slate-500 text-xs font-medium">Sin datos de estancias</p>
      <p class="text-slate-600 text-[11px] mt-1">Registra dispositivos para ver la distribucion</p>
    </div>
  </div>
</template>
