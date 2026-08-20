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
  <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md">
    <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
      <div>
        <h3 class="text-slate-400 text-[10px] font-bold uppercase tracking-widest">Total Consumo</h3>
        <h2 class="text-lg font-extrabold text-white tracking-tight">Rendimiento Energético General</h2>
      </div>

      <div class="flex bg-[#090d16] p-1 rounded-xl border border-white/5 space-x-1 text-xs">
        <button
          @click="activeFilter = 'Mes'"
          :class="activeFilter === 'Mes' ? 'bg-emerald-500 text-slate-950 font-bold shadow' : 'text-slate-400 hover:text-white'"
          class="px-3 py-1 rounded-lg transition"
        >
          Mes
        </button>
        <button
          @click="activeFilter = 'Anual'"
          :class="activeFilter === 'Anual' ? 'bg-emerald-500 text-slate-950 font-bold shadow' : 'text-slate-400 hover:text-white'"
          class="px-3 py-1 rounded-lg transition"
        >
          Anual
        </button>
      </div>
    </div>

    <div class="h-64 w-full">
      <Line :data="lineChartData" :options="lineChartOptions" />
    </div>
  </div>
</template>
