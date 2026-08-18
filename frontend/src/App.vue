<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  Tooltip,
  Legend,
  Title
} from 'chart.js'
import { Doughnut, Line } from 'vue-chartjs'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  Tooltip,
  Legend,
  Title
)

const apiClient = axios.create({
  baseURL: 'http://localhost:8082/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

const isAuthenticated = ref(false)
const userName = ref('')
const currentTab = ref('dashboard')
const activePerformanceFilter = ref('Anual')

const selectedEstanciaId = ref(null)

const loginForm = ref({
  email: '',
  password: ''
})
const loginError = ref('')

const globalData = ref({
  consumoActual: 0,
  costoEstimado: 0.0,
  categoria: 'OPTIMIZADO',
  recomendaciones: []
})
const estanciasDesglose = ref([])

const badgeClass = computed(() => {
  const cat = globalData.value.categoria?.toUpperCase() || ''
  if (cat.includes('MODERADO')) {
    return 'bg-amber-500/10 border-amber-500/20 text-amber-400'
  } else if (cat.includes('INEFICIENTE') || cat.includes('ELEVADO') || cat.includes('ALTO')) {
    return 'bg-rose-500/10 border-rose-500/20 text-rose-400'
  } else {
    return 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
  }
})

const doughnutChartData = computed(() => ({
  labels: estanciasDesglose.value.map(e => e.nombreEstancia),
  datasets: [
    {
      backgroundColor: ['#10b981', '#06b6d4', '#3b82f6', '#f59e0b', '#8b5cf6'],
      data: estanciasDesglose.value.map(e => e.consumoKwh),
      borderWidth: 0,
    }
  ]
}))

const doughnutChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom',
      labels: { color: '#94a3b8', font: { size: 11 } }
    }
  }
}

const lineChartData = {
  labels: ['ENE', 'FEB', 'MAR', 'ABR', 'MAY', 'JUN', 'JUL', 'AGO', 'SEP', 'OCT', 'NOV', 'DIC'],
  datasets: [
    {
      label: 'Consumo Histórico (kWh)',
      borderColor: '#10b981',
      data: [120, 110, 140, 100, 115, 95, 105, 125, 110, 130, 150, 138],
      tension: 0.4,
      fill: true,
      backgroundColor: (context) => {
        const ctx = context.chart.ctx;
        const gradient = ctx.createLinearGradient(0, 0, 0, 200);
        gradient.addColorStop(0, 'rgba(16, 185, 129, 0.3)');
        gradient.addColorStop(1, 'rgba(16, 185, 129, 0.0)');
        return gradient;
      }
    }
  ]
}

const lineChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { display: false } },
  scales: {
    x: { grid: { color: 'rgba(255, 255, 255, 0.03)' }, ticks: { color: '#64748b' } },
    y: { grid: { color: 'rgba(255, 255, 255, 0.03)' }, ticks: { color: '#64748b' } }
  }
}

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

const handleLogin = async () => {
  loginError.value = ''
  try {
    const response = await apiClient.post('/auth/login', {
      email: loginForm.value.email,
      password: loginForm.value.password
    })

    const token = response.data.token || response.data.accessToken || response.data.jwt || response.data
    localStorage.setItem('token', token)

    isAuthenticated.value = true
    await fetchUserProfile()
    await fetchDashboardData()
  } catch (error) {
    console.error("Error en el login:", error)
    loginError.value = 'Credenciales inválidas o error de conexión con la base de datos.'
  }
}

const fetchUserProfile = async () => {
  try {
    const response = await apiClient.get('/usuarios/me')
    if (response.data && response.data.nombre) {
      userName.value = response.data.nombre
    }
  } catch (error) {
    console.error("Error al cargar el perfil del usuario:", error)
  }
}

const fetchDashboardData = async () => {
  try {
    const responseAnalisis = await apiClient.post('/analisis-energetico')
    if (responseAnalisis.data) {
      const data = responseAnalisis.data
      globalData.value.consumoActual = data.consumoActual ?? 0
      globalData.value.costoEstimado = data.costoEstimado ?? 0.0
      globalData.value.categoria = data.categoria ?? 'OPTIMIZADO'

      globalData.value.recomendaciones = data.recomendaciones?.length > 0
        ? data.recomendaciones
        : ["El consumo se encuentra en un rango moderado y equilibrado."]

      if (data.desgloseEstancias) {
        estanciasDesglose.value = data.desgloseEstancias.map(est => ({
          ...est,
          dispositivos: est.dispositivos || null
        }))
      }
    }
  } catch (error) {
    console.error("Error al cargar datos protegidos:", error)
    if (error.response && error.response.status === 403) {
      logout()
    }
  }
}

const toggleEstancia = async (estancia) => {
  const estanciaId = estancia.id || estancia.estanciaId
  if (selectedEstanciaId.value === estanciaId) {
    selectedEstanciaId.value = null
  } else {
    selectedEstanciaId.value = estanciaId

    if (!estancia.dispositivos) {
      try {
        const response = await apiClient.get(`/estancias/${estanciaId}/analisis`)
        if (response.data && response.data.dispositivos) {
          estancia.dispositivos = response.data.dispositivos
        }
      } catch (error) {
        console.error("Error al obtener el análisis de la estancia:", error)
        estancia.dispositivos = []
      }
    }
  }
}

const logout = () => {
  localStorage.removeItem('token')
  isAuthenticated.value = false
  userName.value = ''
}

onMounted(() => {
  const savedToken = localStorage.getItem('token')
  if (savedToken) {
    isAuthenticated.value = true
    fetchUserProfile()
    fetchDashboardData()
  }
})
</script>

<template>
  <div class="min-h-screen bg-[#0d111a] text-slate-100 flex selection:bg-emerald-500 selection:text-slate-950 font-sans">

    <!-- VISTA 1: LOGIN -->
    <div v-if="!isAuthenticated" class="flex-1 flex items-center justify-center p-4 bg-[#090d16]">
      <div class="w-full max-w-md bg-[#121824]/90 backdrop-blur-md border border-white/15 shadow-2xl p-8 rounded-2xl">
        <div class="text-center mb-6">
          <h1 class="text-3xl font-extrabold text-emerald-400 tracking-tight">EnergiAI</h1>
          <p class="text-xs text-slate-400 mt-1">Panel de Control de Eficiencia Energética</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-4">
          <div>
            <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Correo Electrónico</label>
            <input
              type="email"
              v-model="loginForm.email"
              required
              placeholder="admin@energia.com"
              class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            />
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Contraseña</label>
            <input
              type="password"
              v-model="loginForm.password"
              required
              placeholder="••••••••"
              class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            />
          </div>

          <p v-if="loginError" class="text-rose-400 text-xs text-center font-medium">{{ loginError }}</p>

          <button type="submit" class="w-full py-3 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-950 font-bold rounded-xl transition shadow-lg shadow-emerald-500/20 text-sm mt-2">
            Iniciar Sesión
          </button>
        </form>
      </div>
    </div>

    <!-- VISTA 2: PANEL PRINCIPAL CON LAYOUT FIJO -->
    <div v-else class="flex flex-col md:flex-row flex-1 h-screen overflow-hidden">

      <!-- SIDEBAR FIJO -->
      <aside class="w-full md:w-64 bg-[#090d16] border-r border-white/5 flex flex-col justify-between p-6 shrink-0 shadow-2xl h-screen sticky top-0">
        <div>
          <div class="flex items-center space-x-3 mb-8 px-2">
            <div class="w-8 h-8 rounded-lg bg-emerald-500 flex items-center justify-center font-black text-slate-950 shadow-md shadow-emerald-500/30">
              E
            </div>
            <div>
              <h2 class="font-bold text-white tracking-wider text-xs uppercase">EnergiAI Panel</h2>
              <span class="text-[10px] text-slate-400 font-semibold tracking-widest">ECO DASHBOARD</span>
            </div>
          </div>

          <nav class="space-y-1">
            <button
              @click="currentTab = 'dashboard'"
              :class="currentTab === 'dashboard' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
              class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider">
              <span>📊</span>
              <span class="uppercase">Dashboard</span>
            </button>

            <button
              @click="currentTab = 'dispositivos'"
              :class="currentTab === 'dispositivos' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
              class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider">
              <span>🔌</span>
              <span class="uppercase">Dispositivos</span>
            </button>

            <button
              @click="currentTab = 'graficos'"
              :class="currentTab === 'graficos' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
              class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider">
              <span>📈</span>
              <span class="uppercase">Tendencias</span>
            </button>
          </nav>
        </div>

        <div class="pt-4 border-t border-white/5">
          <div class="flex items-center space-x-3 px-2 py-2">
            <div class="w-7 h-7 rounded-full bg-emerald-500/20 border border-emerald-500/30 flex items-center justify-center text-emerald-400 font-bold text-xs">
              {{ userName ? userName.charAt(0).toUpperCase() : 'A' }}
            </div>
            <div class="overflow-hidden">
              <p class="text-xs text-white font-bold truncate">{{ userName || 'Usuario' }}</p>
              <p class="text-[10px] text-emerald-400 uppercase tracking-widest">Activo</p>
            </div>
          </div>
        </div>
      </aside>

      <!-- CONTENEDOR DERECHO CON SCROLL INDEPENDIENTE -->
      <div class="flex-1 flex flex-col h-screen overflow-hidden bg-[#0d111a]">

        <!-- Topbar Estricta y Fija (Altura exacta h-16 y shrink-0 para evitar alteraciones de tamaño) -->
        <header class="h-16 shrink-0 border-b border-white/5 px-8 flex justify-between items-center bg-[#0d111a]/90 backdrop-blur-md z-20">
          <div>
            <h1 class="text-xs font-bold uppercase tracking-widest text-slate-400">Dashboard / <span class="text-white">{{ currentTab }}</span></h1>
          </div>

          <div class="flex items-center space-x-5 text-slate-400">
            <div class="flex items-center space-x-2 pl-2 border-l border-white/10">
              <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
              <button @click="logout" class="text-xs text-rose-400 hover:underline font-semibold">Salir</button>
            </div>
          </div>
        </header>

        <!-- Cuerpo Dinámico con Scroll Independiente -->
        <main class="flex-1 overflow-y-auto p-8 space-y-6">

          <!-- PESTAÑA: PANEL GENERAL -->
          <template v-if="currentTab === 'dashboard'">

            <!-- Gráfico Principal con Selector de Rango -->
            <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md">
              <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
                <div>
                  <h3 class="text-slate-400 text-[10px] font-bold uppercase tracking-widest">Total Consumo</h3>
                  <h2 class="text-lg font-extrabold text-white tracking-tight">Rendimiento Energético General</h2>
                </div>

                <div class="flex bg-[#090d16] p-1 rounded-xl border border-white/5 space-x-1 text-xs">
                  <button
                    @click="activePerformanceFilter = 'Mes'"
                    :class="activePerformanceFilter === 'Mes' ? 'bg-emerald-500 text-slate-950 font-bold shadow' : 'text-slate-400 hover:text-white'"
                    class="px-3 py-1 rounded-lg transition">Mes</button>
                  <button
                    @click="activePerformanceFilter = 'Anual'"
                    :class="activePerformanceFilter === 'Anual' ? 'bg-emerald-500 text-slate-950 font-bold shadow' : 'text-slate-400 hover:text-white'"
                    class="px-3 py-1 rounded-lg transition">Anual</button>
                </div>
              </div>

              <div class="h-64 w-full">
                <Line :data="lineChartData" :options="lineChartOptions" />
              </div>
            </div>

            <!-- Fila de KPIs Globales -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div class="bg-[#121824]/80 border border-white/5 shadow-xl p-6 rounded-2xl flex flex-col justify-between">
                <h3 class="text-slate-400 text-[10px] font-bold uppercase tracking-widest">Consumo Actual</h3>
                <p class="text-3xl font-mono font-black text-white mt-2">{{ globalData.consumoActual }} <span class="text-xs font-sans font-normal text-slate-400">kWh/mes</span></p>
              </div>

              <div class="bg-[#121824]/80 border border-white/5 shadow-xl p-6 rounded-2xl flex flex-col justify-between">
                <h3 class="text-slate-400 text-[10px] font-bold uppercase tracking-widest">Costo Estimado</h3>
                <p class="text-3xl font-mono font-black text-emerald-400 mt-2">$ {{ globalData.costoEstimado.toFixed(2) }}</p>
              </div>

              <div class="bg-[#121824]/80 border border-white/5 shadow-xl p-6 rounded-2xl flex flex-col justify-between">
                <h3 class="text-slate-400 text-[10px] font-bold uppercase tracking-widest">Estado del Sistema</h3>
                <div class="mt-3 flex items-center justify-between">
                  <span :class="badgeClass" class="px-3 py-1 border font-bold rounded-full text-xs uppercase tracking-wider">
                    {{ globalData.categoria }}
                  </span>
                  <span class="text-xs text-slate-400 font-medium">IA Activa</span>
                </div>
              </div>
            </div>

            <!-- Estancias, Dona y Recomendaciones (Alineadas arriba con items-start) -->
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 items-start">

              <!-- Desglose de Estancias -->
              <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col">
                <div class="flex justify-between items-center mb-6">
                  <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest">Estancias</h2>
                  <div class="flex items-center space-x-2">
                    <span class="text-[11px] text-slate-400 font-medium">{{ estanciasDesglose.length }} reg.</span>
                    <button class="w-7 h-7 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 hover:bg-emerald-500 hover:text-slate-950 transition flex items-center justify-center font-bold text-sm" title="Añadir Estancia">+</button>
                  </div>
                </div>

                <div class="space-y-4 max-h-[500px] overflow-y-auto pr-1">
                  <div v-for="estancia in estanciasDesglose" :key="estancia.id || estancia.estanciaId" class="group">

                    <div
                      @click="toggleEstancia(estancia)"
                      :class="selectedEstanciaId === (estancia.id || estancia.estanciaId) ? 'border-emerald-500/50 bg-[#0d1320]' : 'border-white/5 bg-[#090d16] hover:border-white/10'"
                      class="flex justify-between items-center px-5 py-4 rounded-xl border transition-all duration-300 cursor-pointer select-none">
                      <div>
                        <div class="flex items-center space-x-2">
                          <h4 class="font-bold text-white text-sm tracking-wide">{{ estancia.nombreEstancia }}</h4>
                          <span v-if="estancia.dispositivos" class="text-[10px] text-emerald-400 font-mono">({{ estancia.dispositivos.length }} disp.)</span>
                        </div>
                        <p class="text-[11px] text-emerald-400/80 font-mono mt-0.5">{{ estancia.consumoKwh }} kWh total</p>
                      </div>

                      <div class="flex items-center space-x-4">
                        <span class="font-mono font-bold text-white text-sm">$ {{ estancia.costo.toFixed(2) }}</span>
                        <span
                          class="text-slate-500 transition-transform duration-300"
                          :class="{ 'rotate-180 text-emerald-400': selectedEstanciaId === (estancia.id || estancia.estanciaId) }">
                          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                          </svg>
                        </span>
                      </div>
                    </div>

                    <div
                      v-if="selectedEstanciaId === (estancia.id || estancia.estanciaId)"
                      class="mt-2 pl-2 space-y-2 animate-in fade-in slide-in-from-top-2 duration-300">

                      <div v-if="estancia.dispositivos && estancia.dispositivos.length > 0">
                        <div v-for="disp in estancia.dispositivos" :key="disp.id"
                             class="flex justify-between items-center px-4 py-3 bg-[#0f1420] border border-white/5 rounded-xl text-sm">
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
                        <p class="text-slate-500 text-xs italic">Cargando datos o sin dispositivos...</p>
                      </div>
                    </div>

                  </div>

                  <p v-if="estanciasDesglose.length === 0" class="text-slate-500 text-xs text-center py-6">Sin registros.</p>
                </div>
              </div>

              <!-- Gráfico de Dona -->
              <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col justify-between">
                <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest mb-2">Distribución</h2>
                <div class="relative h-44 w-full flex items-center justify-center">
                  <Doughnut :data="doughnutChartData" :options="doughnutChartOptions" />
                </div>
              </div>

              <!-- Recomendaciones -->
              <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col justify-between">
                <div>
                  <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest mb-4">Recomendaciones</h2>
                  <div class="space-y-3">
                    <div v-for="(rec, index) in globalData.recomendaciones" :key="index" class="flex items-start space-x-3 p-3.5 bg-[#090d16]/90 border border-white/5 rounded-xl">
                      <span class="text-emerald-400 text-sm mt-0.5">💡</span>
                      <p class="text-xs text-slate-300 leading-relaxed font-normal">{{ rec }}</p>
                    </div>
                  </div>
                </div>
              </div>

            </div>

          </template>

          <!-- PESTAÑA: DISPOSITIVOS -->
          <template v-if="currentTab === 'dispositivos'">
            <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-8 shadow-xl text-center py-20">
              <h2 class="text-xl font-bold text-white mb-2">Gestión de Dispositivos</h2>
              <p class="text-slate-400 text-sm">Módulo listo para implementar el CRUD completo de artefactos eléctricos.</p>
            </div>
          </template>

          <!-- PESTAÑA: TENDENCIAS -->
          <template v-if="currentTab === 'graficos'">
            <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-8 shadow-xl space-y-6">
              <h2 class="text-xl font-bold text-white mb-2">Análisis Histórico Avanzado</h2>
              <div class="h-80 w-full">
                <Line :data="lineChartData" :options="lineChartOptions" />
              </div>
            </div>
          </template>

          <!-- Footer integrado al final del scroll -->
          <footer class="text-center py-4 text-[10px] text-slate-500 border-t border-white/5 uppercase tracking-widest">
            EnergiAI • Eco Dashboard Edition
          </footer>

        </main>
      </div>

    </div>
  </div>
</template>