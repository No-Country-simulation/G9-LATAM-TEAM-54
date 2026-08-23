<script setup>
import { ref, computed } from "vue"
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Tooltip,
  Legend,
  Title,
  Filler
} from "chart.js"
import { Line, Bar } from "vue-chartjs"

// Asegurar registro de componentes Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Tooltip,
  Legend,
  Title,
  Filler
)

const props = defineProps({
  historialReportes: {
    type: Array,
    default: () => []
  },
  dispositivos: {
    type: Array,
    default: () => []
  },
  estanciasDesglose: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(["abrir-modal-dispositivo"])

// Toggle entre vistas: 'historico' | 'dispositivos'
const activeView = ref("historico")

// ── Obtención y Normalización de Dispositivos ──────────────────────────────
const listaDispositivos = computed(() => {
  // 1. Si se pasa el arreglo de dispositivos directamente
  if (props.dispositivos && props.dispositivos.length > 0) {
    return [...props.dispositivos]
  }

  // 2. Fallback: extraer dispositivos anidados dentro de estanciasDesglose
  const extraidos = []
  if (props.estanciasDesglose && Array.isArray(props.estanciasDesglose)) {
    props.estanciasDesglose.forEach(est => {
      if (est.dispositivos && Array.isArray(est.dispositivos)) {
        est.dispositivos.forEach(d => {
          extraidos.push({
            id: d.id,
            alias: d.alias || d.nombreEquipo || "Dispositivo",
            nombreEquipo: d.nombreEquipo,
            nombreEstancia: d.nombreEstancia || est.nombreEstancia,
            consumoMensualKwh: d.consumoMensualKwh ?? 0,
            horasUsoDiarias: d.horasUsoDiarias
          })
        })
      }
    })
  }

  return extraidos
})

const dispositivosOrdenados = computed(() =>
  [...listaDispositivos.value].sort((a, b) => (b.consumoMensualKwh || 0) - (a.consumoMensualKwh || 0))
)

// Función para formatear etiquetas largas en multilínea sin truncar nombres
const formatLabelMultilinea = (texto) => {
  if (!texto) return "Dispositivo"
  if (texto.length <= 16) return texto
  const palabras = texto.split(" ")
  if (palabras.length <= 1) return texto
  const mitad = Math.ceil(palabras.length / 2)
  return [
    palabras.slice(0, mitad).join(" "),
    palabras.slice(mitad).join(" ")
  ]
}

// ── Vista de Tendencia Histórica ───────────────────────────────────────────
const reportesOrdenados = computed(() =>
  [...props.historialReportes]
    .sort((a, b) => new Date(a.fechaCreacion || a.fecha_creacion) - new Date(b.fechaCreacion || b.fecha_creacion))
    .slice(-12)
)

const labelsFecha = computed(() =>
  reportesOrdenados.value.map(r => {
    const d = new Date(r.fechaCreacion || r.fecha_creacion)
    return isNaN(d.getTime())
      ? `#${r.id}`
      : d.toLocaleDateString("es-ES", { day: "2-digit", month: "short" })
  })
)

const dataConsumo = computed(() =>
  reportesOrdenados.value.map(r => r.consumoActual ?? r.consumo_actual ?? 0)
)

const lineChartData = computed(() => ({
  labels: labelsFecha.value,
  datasets: [
    {
      label: "Consumo Histórico (kWh)",
      borderColor: "#10b981",
      pointBackgroundColor: "#10b981",
      pointBorderColor: "#121824",
      pointRadius: 5,
      pointHoverRadius: 7,
      data: dataConsumo.value,
      tension: 0.4,
      fill: true,
      backgroundColor: (context) => {
        const ctx = context.chart?.ctx
        if (!ctx) return "rgba(16, 185, 129, 0.1)"
        const gradient = ctx.createLinearGradient(0, 0, 0, 200)
        gradient.addColorStop(0, "rgba(16, 185, 129, 0.28)")
        gradient.addColorStop(1, "rgba(16, 185, 129, 0.0)")
        return gradient
      }
    }
  ]
}))

const lineChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
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
        label: (ctx) => ` Consumo: ${ctx.parsed.y} kWh/mes`
      }
    }
  },
  scales: {
    x: {
      grid: { color: "rgba(255, 255, 255, 0.03)" },
      ticks: { color: "#64748b", font: { size: 10 } }
    },
    y: {
      grid: { color: "rgba(255, 255, 255, 0.03)" },
      ticks: { color: "#64748b", font: { size: 10 } }
    }
  }
}

// ── Vista de Desglose por Dispositivos (Columnas Verticales Optimizadas) ───
const barChartData = computed(() => ({
  labels: dispositivosOrdenados.value.map(d => formatLabelMultilinea(d.alias || d.nombreEquipo)),
  datasets: [
    {
      label: "Consumo Individual",
      borderRadius: { topLeft: 8, topRight: 8, bottomLeft: 0, bottomRight: 0 },
      borderWidth: 1.5,
      borderColor: "#06b6d4",
      hoverBorderColor: "#38bdf8",
      maxBarThickness: 68, // Ancho optimizado para destacar con presencia cuando hay 1, 2 o pocos dispositivos
      barPercentage: 0.6,
      categoryPercentage: 0.72,
      backgroundColor: (context) => {
        const ctx = context.chart?.ctx
        const chartArea = context.chart?.chartArea
        if (!ctx || !chartArea) return "rgba(6, 182, 212, 0.7)"
        const gradient = ctx.createLinearGradient(0, chartArea.top, 0, chartArea.bottom)
        gradient.addColorStop(0, "rgba(6, 182, 212, 0.9)")    // Cian neón superior
        gradient.addColorStop(0.5, "rgba(16, 185, 129, 0.75)") // Verde esmeralda central
        gradient.addColorStop(1, "rgba(16, 185, 129, 0.12)")  // Base translúcida sutil
        return gradient
      },
      hoverBackgroundColor: (context) => {
        const ctx = context.chart?.ctx
        const chartArea = context.chart?.chartArea
        if (!ctx || !chartArea) return "rgba(6, 182, 212, 0.95)"
        const gradient = ctx.createLinearGradient(0, chartArea.top, 0, chartArea.bottom)
        gradient.addColorStop(0, "rgba(6, 182, 212, 1)")
        gradient.addColorStop(0.5, "rgba(16, 185, 129, 0.9)")
        gradient.addColorStop(1, "rgba(16, 185, 129, 0.25)")
        return gradient
      },
      data: dispositivosOrdenados.value.map(d => Number((d.consumoMensualKwh || 0).toFixed(1)))
    }
  ]
}))

const barChartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: "#0c121e",
      borderColor: "rgba(6, 182, 212, 0.3)",
      borderWidth: 1,
      titleColor: "#f8fafc",
      bodyColor: "#94a3b8",
      padding: 11,
      cornerRadius: 10,
      callbacks: {
        title: (items) => {
          const idx = items[0]?.dataIndex
          const disp = dispositivosOrdenados.value[idx]
          return disp ? `${disp.alias || disp.nombreEquipo}` : ''
        },
        label: (ctx) => {
          const idx = ctx.dataIndex
          const disp = dispositivosOrdenados.value[idx]
          const estancia = disp?.nombreEstancia ? `Estancia: ${disp.nombreEstancia}` : ''
          const horas = disp?.horasUsoDiarias != null ? `Uso: ${disp.horasUsoDiarias} hrs/día` : ''
          const lineas = [`Consumo: ${ctx.parsed.y} kWh/mes`]
          if (estancia) lineas.push(estancia)
          if (horas) lineas.push(horas)
          return lineas
        }
      }
    }
  },
  scales: {
    x: {
      grid: { display: false },
      ticks: {
        color: "#cbd5e1",
        font: { size: 11, weight: "600" },
        autoSkip: false,
        maxRotation: 0,
        minRotation: 0,
        padding: 6
      }
    },
    y: {
      beginAtZero: true,
      grid: { color: "rgba(255, 255, 255, 0.04)" },
      ticks: {
        color: "#64748b",
        font: { size: 10 },
        callback: (val) => `${val} kWh`
      }
    }
  }
}))

const sinDatos = computed(() =>
  activeView.value === "historico"
    ? reportesOrdenados.value.length === 0
    : dispositivosOrdenados.value.length === 0
)
</script>

<template>
  <div class="h-[390px] bg-[#121824]/90 border border-white/5 rounded-2xl p-5 sm:p-6 shadow-xl backdrop-blur-xl flex flex-col">
    <!-- Header + Toggle -->
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center pb-3 gap-3 shrink-0">
      <div>
        <h3 class="text-slate-400 text-[10px] font-extrabold uppercase tracking-widest">Rendimiento Energético</h3>
        <h2 class="text-base font-extrabold text-white tracking-tight">
          {{ activeView === 'historico' ? 'Tendencia Histórica' : 'Desglose por Dispositivos' }}
        </h2>
      </div>

      <!-- Tab Toggle -->
      <div class="flex bg-[#090d16] p-1 rounded-xl border border-white/5 space-x-1 text-xs shrink-0">
        <button
          @click="activeView = 'historico'"
          :class="activeView === 'historico' ? 'bg-emerald-500 text-slate-950 font-black shadow-md' : 'text-slate-400 hover:text-white font-semibold'"
          class="px-3 py-1.5 rounded-lg transition-all duration-200 cursor-pointer inline-flex items-center gap-1.5 leading-none"
        >
          <svg class="w-3 h-3 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
          </svg>
          <span>Histórico</span>
        </button>
        <button
          @click="activeView = 'dispositivos'"
          :class="activeView === 'dispositivos' ? 'bg-emerald-500 text-slate-950 font-black shadow-md' : 'text-slate-400 hover:text-white font-semibold'"
          class="px-3 py-1.5 rounded-lg transition-all duration-200 cursor-pointer inline-flex items-center gap-1.5 leading-none"
        >
          <svg class="w-3 h-3 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 22v-5"/>
            <path d="M9 8V2"/>
            <path d="M15 8V2"/>
            <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
          </svg>
          <span>Por Dispositivos</span>
        </button>
      </div>
    </div>

    <!-- Chart area -->
    <div class="flex-1 min-h-0 w-full pt-2">

      <!-- Histórico: gráfico de línea -->
      <transition name="view-slide" mode="out-in">
        <div v-if="activeView === 'historico'" key="historico" class="h-full flex flex-col">
          <div v-if="sinDatos" class="flex-1 flex flex-col items-center justify-center text-center py-6">
            <div class="w-12 h-12 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 mb-3 shadow-inner">
              <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
                <polyline points="17 6 23 6 23 12"/>
              </svg>
            </div>
            <p class="text-slate-300 text-sm font-bold">Sin reportes históricos aún</p>
            <p class="text-slate-500 text-xs mt-1 max-w-xs leading-relaxed">Genera tu primer análisis con el Motor IA desde la sección <span class="text-emerald-400 font-semibold">Reportes</span> para ver la tendencia de consumo.</p>
          </div>
          <Line v-else :data="lineChartData" :options="lineChartOptions" class="flex-1 min-h-0" />
        </div>

        <!-- Dispositivos: gráfico de columnas verticales con gradiente cian/esmeralda -->
        <div v-else key="dispositivos" class="h-full flex flex-col">
          <div v-if="sinDatos" class="flex-1 flex flex-col items-center justify-center text-center py-6">
            <div class="w-12 h-12 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 mb-3 shadow-inner">
              <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M12 22v-5"/>
                <path d="M9 8V2"/>
                <path d="M15 8V2"/>
                <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
              </svg>
            </div>
            <p class="text-slate-300 text-sm font-bold">Sin dispositivos registrados</p>
            <p class="text-slate-500 text-xs mt-1 max-w-xs leading-relaxed">Registra tus equipos para visualizar el desglose de consumo individual por dispositivo.</p>
            <button
              @click="emit('abrir-modal-dispositivo')"
              class="mt-3 px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20 inline-flex items-center gap-1.5 leading-none cursor-pointer active:scale-[0.98]"
            >
              <svg class="w-3 h-3 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="12" y1="5" x2="12" y2="19"/>
                <line x1="5" y1="12" x2="19" y2="12"/>
              </svg>
              <span>Añadir Primer Dispositivo</span>
            </button>
          </div>
          <Bar v-else :data="barChartData" :options="barChartOptions" class="flex-1 min-h-0" />
        </div>
      </transition>
    </div>
  </div>
</template>
