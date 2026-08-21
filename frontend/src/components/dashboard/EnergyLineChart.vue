<script setup>
import { ref } from "vue"
import { Line } from "vue-chartjs"

const activeFilter = ref("Anual")

const lineChartData = {
  labels: ["ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"],
  datasets: [
    {
      label: "Consumo Histórico (kWh)",
      borderColor: "#10b981",
      data: [120, 110, 140, 100, 115, 95, 105, 125, 110, 130, 150, 138],
      tension: 0.4,
      fill: true,
      backgroundColor: (context) => {
        const ctx = context.chart.ctx
        const gradient = ctx.createLinearGradient(0, 0, 0, 200)
        gradient.addColorStop(0, "rgba(16, 185, 129, 0.3)")
        gradient.addColorStop(1, "rgba(16, 185, 129, 0.0)")
        return gradient
      }
    }
  ]
}

const lineChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { display: false } },
  scales: {
    x: { grid: { color: "rgba(255, 255, 255, 0.03)" }, ticks: { color: "#64748b" } },
    y: { grid: { color: "rgba(255, 255, 255, 0.03)" }, ticks: { color: "#64748b" } }
  }
}
</script>

<template>
  <div class="h-[390px] bg-[#121824]/90 border border-white/5 rounded-2xl p-5 sm:p-6 shadow-xl backdrop-blur-xl flex flex-col justify-between">
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center pb-3 gap-3">
      <div>
        <h3 class="text-slate-400 text-[10px] font-extrabold uppercase tracking-widest">Total Consumo</h3>
        <h2 class="text-base font-extrabold text-white tracking-tight">Rendimiento Energético General</h2>
      </div>

      <div class="flex bg-[#090d16] p-1 rounded-xl border border-white/5 space-x-1 text-xs">
        <button
          @click="activeFilter = 'Mes'"
          :class="activeFilter === 'Mes' ? 'bg-emerald-500 text-slate-950 font-black shadow-md' : 'text-slate-400 hover:text-white font-semibold'"
          class="px-3 py-1 rounded-lg transition-all duration-200 cursor-pointer"
        >
          Mes
        </button>
        <button
          @click="activeFilter = 'Anual'"
          :class="activeFilter === 'Anual' ? 'bg-emerald-500 text-slate-950 font-black shadow-md' : 'text-slate-400 hover:text-white font-semibold'"
          class="px-3 py-1 rounded-lg transition-all duration-200 cursor-pointer"
        >
          Anual
        </button>
      </div>
    </div>

    <div class="flex-1 min-h-0 w-full pt-2">
      <Line :data="lineChartData" :options="lineChartOptions" />
    </div>
  </div>
</template>
