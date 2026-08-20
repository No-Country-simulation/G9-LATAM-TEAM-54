<script setup>
import { ref, onMounted, computed, watch } from 'vue'
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
  Title,
  Filler
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
  Title,
  Filler
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
const authView = ref('landing')
const needsSetup = ref(false)

const selectedEstanciaId = ref(null)

const abrirModalDispositivo = ref(false)
const estanciasDisponibles = ref([])
const tiposDispositivosDisponibles = ref([])
const variantesDisponibles = ref([])
const dispositivosRegistrados = ref([])
const cargandoDispositivos = ref(false)

const historialReportes = ref([])
const cargandoReportes = ref(false)
const generandoReporte = ref(false)

// Estado para el Toast flotante
const toastMessage = ref('')
const showToast = ref(false)
let toastTimeout = null

const triggerToast = (msg) => {
  if (toastTimeout) clearTimeout(toastTimeout)
  toastMessage.value = msg
  showToast.value = true
  toastTimeout = setTimeout(() => {
    showToast.value = false
  }, 3500)
}

const nuevoDispositivoForm = ref({
  estanciaId: '',
  equipoId: '',
  varianteId: '',
  alias: '',
  horasUsoDiarias: 4
})
const dispositivoError = ref('')

const loginForm = ref({
  email: '',
  password: ''
})
const loginError = ref('')

const registerForm = ref({
  nombre: '',
  email: '',
  password: ''
})
const registerError = ref('')
const registerSuccess = ref('')

const opcionesTemperatura = ref([])

const setupForm = ref({
  avgTemperatureC: null,
  householdSize: 2
})
const setupError = ref('')

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

const getBadgeClassForCat = (catStr) => {
  const cat = catStr?.toUpperCase() || ''
  if (cat.includes('MODERADO')) {
    return 'bg-amber-500/10 border-amber-500/20 text-amber-400'
  } else if (cat.includes('INEFICIENTE') || cat.includes('ELEVADO') || cat.includes('ALTO')) {
    return 'bg-rose-500/10 border-rose-500/20 text-rose-400'
  } else {
    return 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
  }
}

const itemSeleccionadoCatalogo = computed(() => {
  return tiposDispositivosDisponibles.value.find(
    (t) => (t.id || t.equipoId) === nuevoDispositivoForm.value.equipoId
  )
})

const tieneVariantesSeleccionado = computed(() => {
  const item = itemSeleccionadoCatalogo.value
  const val = item?.tieneVariantes ?? item?.tiene_variantes
  return val === 1 || val === true || val === '1'
})

watch(() => nuevoDispositivoForm.value.equipoId, async (newEquipoId) => {
  nuevoDispositivoForm.value.varianteId = ''
  variantesDisponibles.value = []

  if (newEquipoId) {
    try {
      const response = await apiClient.get(`/catalogo/${newEquipoId}/variantes`)
      if (response.data && Array.isArray(response.data)) {
        variantesDisponibles.value = response.data
      }
    } catch (err) {
      console.error(err)
    }
  }
})

watch(currentTab, (newTab) => {
  if (newTab === 'dispositivos') {
    fetchMisDispositivos()
  } else if (newTab === 'reportes') {
    fetchHistorialReportes()
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

const fetchOpcionesTemperatura = async () => {
  try {
    const response = await apiClient.get('/configuracion/opciones-temperatura')
    if (response.data) {
      opcionesTemperatura.value = response.data
      if (response.data.length > 0 && setupForm.value.avgTemperatureC === null) {
        setupForm.value.avgTemperatureC = response.data[0].valorNumerico
      }
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchEstanciasDB = async () => {
  try {
    const response = await apiClient.get('/estancias')
    if (response.data) {
      estanciasDisponibles.value = response.data
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchTiposDispositivosDB = async () => {
  try {
    const response = await apiClient.get('/catalogo')
    if (response.data) {
      tiposDispositivosDisponibles.value = response.data
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchMisDispositivos = async () => {
  cargandoDispositivos.value = true
  try {
    const response = await apiClient.get('/dispositivos')
    if (response.data && Array.isArray(response.data)) {
      dispositivosRegistrados.value = response.data
    }
  } catch (error) {
    console.error(error)
  } finally {
    cargandoDispositivos.value = false
  }
}

const fetchHistorialReportes = async () => {
  cargandoReportes.value = true
  try {
    const response = await apiClient.get('/historial')
    if (response.data && Array.isArray(response.data)) {
      historialReportes.value = response.data
    }
  } catch (error) {
    console.error(error)
    try {
      const fallbackResponse = await apiClient.get('/analisis-energetico/historial')
      if (fallbackResponse.data && Array.isArray(fallbackResponse.data)) {
        historialReportes.value = fallbackResponse.data
      }
    } catch (err) {
      console.error(err)
    }
  } finally {
    cargandoReportes.value = false
  }
}

const generarNuevoReporte = async () => {
  if (generandoReporte.value) return
  generandoReporte.value = true
  try {
    await apiClient.post('/analisis-energetico')
    await fetchDashboardData()
    if (currentTab.value === 'reportes') {
      await fetchHistorialReportes()
    }
    triggerToast('¡Reporte generado y guardado en base de datos exitosamente!')
  } catch (error) {
    console.error(error)
    triggerToast('Error al generar el reporte energético.')
  } finally {
    generandoReporte.value = false
  }
}

const eliminarDispositivo = async (id) => {
  if (!confirm('¿Estás seguro de que deseas eliminar este dispositivo?')) return
  try {
    await apiClient.delete(`/dispositivos/${id}`)
    await fetchMisDispositivos()
    await fetchDashboardData()
  } catch (error) {
    console.error(error)
    alert('No se pudo eliminar el dispositivo.')
  }
}

const abrirModalCrearDispositivo = async () => {
  await fetchEstanciasDB()
  await fetchTiposDispositivosDB()
  abrirModalDispositivo.value = true
}

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
  } catch (error) {
    console.error(error)
    loginError.value = 'Credenciales inválidas o error de conexión con la base de datos.'
  }
}

const handleRegister = async () => {
  registerError.value = ''
  registerSuccess.value = ''
  try {
    await apiClient.post('/usuarios', {
      nombre: registerForm.value.nombre,
      email: registerForm.value.email,
      password: registerForm.value.password
    })
    registerSuccess.value = '¡Cuenta creada con éxito! Ahora puedes iniciar sesión.'
    setTimeout(() => {
      authView.value = 'login'
    }, 2000)
  } catch (error) {
    console.error(error)
    registerError.value = 'No se pudo completar el registro. Verifica los datos.'
  }
}

const handleSetupSubmit = async () => {
  setupError.value = ''
  try {
    await apiClient.put('/usuarios/configuracion-inicial', {
      avgTemperatureC: setupForm.value.avgTemperatureC,
      householdSize: setupForm.value.householdSize
    })
    needsSetup.value = false
    await fetchDashboardData()
  } catch (error) {
    console.error(error)
    setupError.value = 'No se pudo guardar la configuración. Intenta nuevamente.'
  }
}

const handleCrearDispositivo = async () => {
  dispositivoError.value = ''
  try {
    const payload = {
      estanciaId: Number(nuevoDispositivoForm.value.estanciaId),
      equipoCatalogoId: Number(nuevoDispositivoForm.value.equipoId),
      alias: nuevoDispositivoForm.value.alias,
      horasUsoDiarias: Number(nuevoDispositivoForm.value.horasUsoDiarias)
    }

    if (tieneVariantesSeleccionado.value && nuevoDispositivoForm.value.varianteId) {
      payload.equipoVarianteId = Number(nuevoDispositivoForm.value.varianteId)
    }

    await apiClient.post('/dispositivos/guardar', payload)

    nuevoDispositivoForm.value.alias = ''
    nuevoDispositivoForm.value.estanciaId = ''
    nuevoDispositivoForm.value.equipoId = ''
    nuevoDispositivoForm.value.varianteId = ''
    nuevoDispositivoForm.value.horasUsoDiarias = 4
    abrirModalDispositivo.value = false

    await fetchDashboardData()
    if (currentTab.value === 'dispositivos') {
      await fetchMisDispositivos()
    }
  } catch (error) {
    console.error(error)
    dispositivoError.value = 'No se pudo registrar el dispositivo. Verifica los datos.'
  }
}

const fetchUserProfile = async () => {
  try {
    const response = await apiClient.get('/usuarios/me')
    if (response.data) {
      userName.value = response.data.nombre || response.data.name || 'Usuario'

      const avgTemp = response.data.avgTemperatureC ?? response.data.avgTemperature ?? response.data.temperaturaPromedio
      const houseSize = response.data.householdSize ?? response.data.household_size ?? response.data.miembrosHogar

      if (avgTemp === null || avgTemp === undefined || avgTemp === 0 || houseSize === null || houseSize === undefined || houseSize === 0) {
        needsSetup.value = true
        globalData.value = { consumoActual: 0, costoEstimado: 0.0, categoria: '', recomendaciones: [] }
        estanciasDesglose.value = []
        await fetchOpcionesTemperatura()
      } else {
        needsSetup.value = false
        await fetchDashboardData()
      }
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchDashboardData = async () => {
  try {
    const response = await apiClient.get('/dashboard/actual')
    if (response.data) {
      const data = response.data
      globalData.value.consumoActual = data.consumoTotal ?? 0
      globalData.value.costoEstimado = data.costoTotal ?? 0.0
      globalData.value.categoria = data.categoria ?? 'OPTIMIZADO'

      globalData.value.recomendaciones = data.recomendaciones?.length > 0
        ? data.recomendaciones
        : ["El consumo se encuentra en un rango moderado y equilibrado."]

      const estanciasCrudas = data.desgloseEstancias || []
      if (estanciasCrudas.length > 0) {
        estanciasDesglose.value = estanciasCrudas.map(est => ({
          ...est,
          id: est.id || est.estanciaId,
          nombreEstancia: est.nombreEstancia || 'Estancia',
          consumoKwh: est.consumoKwh ?? 0,
          costo: est.costo ?? 0.0,
          dispositivos: est.dispositivos || null
        }))
      } else {
        estanciasDesglose.value = []
      }
    }
  } catch (error) {
    console.error(error)
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
        console.error(error)
        estancia.dispositivos = []
      }
    }
  }
}

const logout = () => {
  localStorage.removeItem('token')
  isAuthenticated.value = false
  userName.value = ''
  needsSetup.value = false
  authView.value = 'landing'
  globalData.value = { consumoActual: 0, costoEstimado: 0.0, categoria: 'OPTIMIZADO', recomendaciones: [] }
  estanciasDesglose.value = []
}

onMounted(() => {
  const savedToken = localStorage.getItem('token')
  if (savedToken) {
    isAuthenticated.value = true
    fetchUserProfile()
  }
})
</script>

<template>
  <div class="min-h-screen bg-[#0d111a] text-slate-100 flex selection:bg-emerald-500 selection:text-slate-950 font-sans relative overflow-x-hidden">

    <!-- Toast Flotante Sutil -->
    <transition
      enter-active-class="transform transition ease-out duration-300"
      enter-from-class="translate-y-2 opacity-0"
      enter-to-class="translate-y-0 opacity-100"
      leave-active-class="transform transition ease-in duration-200"
      leave-from-class="translate-y-0 opacity-100"
      leave-to-class="translate-y-2 opacity-0"
    >
      <div v-if="showToast" class="fixed bottom-6 right-6 z-[99999] bg-[#121824] border border-emerald-500/30 text-white px-5 py-3 rounded-2xl shadow-2xl flex items-center space-x-3 backdrop-blur-md">
        <span class="w-2.5 h-2.5 rounded-full bg-emerald-500 animate-pulse"></span>
        <p class="text-xs font-medium">{{ toastMessage }}</p>
      </div>
    </transition>

    <div v-if="!isAuthenticated" class="flex-1 flex flex-col justify-between bg-[#090d16] w-full">
      <header class="h-20 px-8 md:px-16 flex justify-between items-center border-b border-white/5">
        <div class="flex items-center space-x-3">
          <div class="w-9 h-9 rounded-xl bg-emerald-500 flex items-center justify-center font-black text-slate-950 shadow-lg shadow-emerald-500/30">
            E
          </div>
          <span class="text-lg font-extrabold text-white tracking-tight">EnergiAI</span>
        </div>
        <div class="flex items-center space-x-4">
          <button @click="authView = 'landing'" class="text-xs text-slate-300 hover:text-white transition font-semibold">Inicio</button>
          <button @click="authView = 'login'" class="px-4 py-2 text-xs font-bold text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 rounded-xl hover:bg-emerald-500/20 transition">Iniciar Sesión</button>
          <button @click="authView = 'register'" class="px-4 py-2 text-xs font-bold text-slate-950 bg-emerald-500 rounded-xl hover:bg-emerald-400 transition shadow-lg shadow-emerald-500/20">Registrarse</button>
        </div>
      </header>

      <div v-if="authView === 'landing'" class="flex-1 flex flex-col items-center justify-center text-center px-4 max-w-4xl mx-auto py-12">
        <span class="px-3 py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-bold uppercase tracking-widest mb-6">
          Inteligencia Artificial para el Hogar
        </span>
        <h1 class="text-4xl md:text-6xl font-black text-white tracking-tight leading-tight mb-6">
          Optimiza tu consumo energético con <span class="text-emerald-400">EnergiAI</span>
        </h1>
        <p class="text-slate-400 text-sm md:text-base max-w-2xl mb-8 leading-relaxed">
          Analiza el rendimiento eléctrico de tus estancias, gestiona dispositivos por habitación y recibe recomendaciones inteligentes impulsadas por machine learning para reducir tu factura eléctrica.
        </p>
        <div class="flex space-x-4">
          <button @click="authView = 'register'" class="px-6 py-3.5 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-950 font-bold rounded-xl transition shadow-xl shadow-emerald-500/20 text-sm">
            Comenzar Gratis
          </button>
          <button @click="authView = 'login'" class="px-6 py-3.5 bg-[#121824] hover:bg-white/5 border border-white/10 text-white font-bold rounded-xl transition text-sm">
            Acceder a mi cuenta
          </button>
        </div>
      </div>

      <div v-else-if="authView === 'login'" class="flex-1 flex items-center justify-center p-4">
        <div class="w-full max-w-md bg-[#121824]/90 backdrop-blur-md border border-white/15 shadow-2xl p-8 rounded-2xl">
          <div class="text-center mb-6">
            <h2 class="text-2xl font-extrabold text-white tracking-tight">Bienvenido de nuevo</h2>
            <p class="text-xs text-slate-400 mt-1">Ingresa tus credenciales para acceder al panel</p>
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

          <p class="text-xs text-center text-slate-400 mt-6">
            ¿No tienes cuenta? <button @click="authView = 'register'" class="text-emerald-400 font-bold hover:underline">Regístrate aquí</button>
          </p>
        </div>
      </div>

      <div v-else-if="authView === 'register'" class="flex-1 flex items-center justify-center p-4">
        <div class="w-full max-w-md bg-[#121824]/90 backdrop-blur-md border border-white/15 shadow-2xl p-8 rounded-2xl">
          <div class="text-center mb-6">
            <h2 class="text-2xl font-extrabold text-white tracking-tight">Crea tu cuenta</h2>
            <p class="text-xs text-slate-400 mt-1">Conéctate a la plataforma EnergiAI</p>
          </div>

          <form @submit.prevent="handleRegister" class="space-y-4">
            <div>
              <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Nombre Completo</label>
              <input
                type="text"
                v-model="registerForm.nombre"
                required
                placeholder="Ej. Adrián Pérez"
                class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
              />
            </div>

            <div>
              <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Correo Electrónico</label>
              <input
                type="email"
                v-model="registerForm.email"
                required
                placeholder="correo@energia.com"
                class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
              />
            </div>

            <div>
              <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Contraseña</label>
              <input
                type="password"
                v-model="registerForm.password"
                required
                placeholder="••••••••"
                class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
              />
            </div>

            <p v-if="registerError" class="text-rose-400 text-xs text-center font-medium">{{ registerError }}</p>
            <p v-if="registerSuccess" class="text-emerald-400 text-xs text-center font-medium">{{ registerSuccess }}</p>

            <button type="submit" class="w-full py-3 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-950 font-bold rounded-xl transition shadow-lg shadow-emerald-500/20 text-sm mt-2">
              Completar Registro
            </button>
          </form>

          <p class="text-xs text-center text-slate-400 mt-6">
            ¿Ya tienes una cuenta? <button @click="authView = 'login'" class="text-emerald-400 font-bold hover:underline">Inicia sesión</button>
          </p>
        </div>
      </div>

      <footer class="text-center py-6 text-[10px] text-slate-500 border-t border-white/5 uppercase tracking-widest">
        EnergiAI • Todos los derechos reservados
      </footer>
    </div>

    <div v-else-if="needsSetup" class="fixed inset-0 z-50 bg-[#090d16]/95 backdrop-blur-xl flex items-center justify-center p-4 w-full h-screen">
      <div class="w-full max-w-lg bg-[#121824] border border-emerald-500/40 shadow-2xl p-8 rounded-2xl space-y-6">
        <div class="text-center">
          <div class="inline-flex items-center justify-center w-12 h-12 rounded-2xl bg-emerald-500/10 text-emerald-400 mb-3 shadow-inner">
            ⚡
          </div>
          <span class="px-3 py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-[10px] font-bold uppercase tracking-widest">
            Configuración Requerida
          </span>
          <h2 class="text-2xl font-extrabold text-white tracking-tight mt-3">Personalicemos tu entorno</h2>
          <p class="text-xs text-slate-400 mt-1 max-w-sm mx-auto leading-relaxed">
            Para calibrar nuestros algoritmos de predicción y ofrecerte un cálculo de costos preciso, necesitamos conocer un par de detalles clave sobre tu espacio.
          </p>
        </div>

        <form @submit.prevent="handleSetupSubmit" class="space-y-4">
          <div>
            <label class="block text-xs font-semibold text-slate-300 mb-1 uppercase tracking-wider">Temperatura Promedio de la Zona</label>
            <select
              v-model.number="setupForm.avgTemperatureC"
              required
              class="w-full px-4 py-3 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            >
              <option v-for="op in opcionesTemperatura" :key="op.id" :value="op.valorNumerico">
                {{ op.etiqueta }}
              </option>
            </select>
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-300 mb-1 uppercase tracking-wider">Miembros en el hogar u oficina</label>
            <input
              type="number"
              min="1"
              max="50"
              v-model.number="setupForm.householdSize"
              required
              class="w-full px-4 py-3 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            />
          </div>

          <p v-if="setupError" class="text-rose-400 text-xs text-center font-medium">{{ setupError }}</p>

          <button type="submit" class="w-full py-3.5 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-950 font-bold rounded-xl transition shadow-lg shadow-emerald-500/20 text-sm mt-2">
            Guardar y Entrar al Dashboard
          </button>
        </form>
      </div>
    </div>

    <div v-else class="flex flex-col md:flex-row flex-1 h-screen overflow-hidden w-full">
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
              @click="currentTab = 'reportes'"
              :class="currentTab === 'reportes' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
              class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider">
              <span>📋</span>
              <span class="uppercase">Reportes</span>
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

      <div class="flex-1 flex flex-col h-screen overflow-hidden bg-[#0d111a]">
        <header class="h-16 shrink-0 border-b border-white/5 px-8 flex justify-between items-center bg-[#0d111a]/90 backdrop-blur-md z-10">
          <div>
            <h1 class="text-xs font-bold uppercase tracking-widest text-slate-400">Dashboard / <span class="text-white">{{ currentTab }}</span></h1>
          </div>
          <div class="flex items-center space-x-4">
            <button
              @click="generarNuevoReporte"
              :disabled="generandoReporte"
              class="px-3.5 py-1.5 bg-emerald-500/10 hover:bg-emerald-500/20 border border-emerald-500/30 text-emerald-400 rounded-xl font-bold text-xs transition flex items-center space-x-1.5 shadow-sm disabled:opacity-50"
            >
              <span>⚡</span>
              <span>{{ generandoReporte ? 'Generando...' : 'Generar Reporte' }}</span>
            </button>
            <div class="flex items-center space-x-2 pl-3 border-l border-white/10">
              <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
              <button @click="logout" class="text-xs text-rose-400 hover:underline font-semibold">Salir</button>
            </div>
          </div>
        </header>

        <main class="flex-1 overflow-y-auto p-8 space-y-6">
          <template v-if="currentTab === 'dashboard'">
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

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 items-start">
              <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col">
                <div class="flex justify-between items-center mb-6">
                  <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest">Estancias</h2>
                  <div class="flex items-center space-x-2">
                    <span class="text-[11px] text-slate-400 font-medium">{{ estanciasDesglose.length }} reg.</span>
                    <button @click="abrirModalCrearDispositivo" class="px-2.5 py-1 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 hover:bg-emerald-500 hover:text-slate-950 transition flex items-center space-x-1 font-bold text-xs" title="Añadir Dispositivo">
                      <span>+ Dispositivo</span>
                    </button>
                  </div>
                </div>

                <div class="space-y-4 max-h-[500px] overflow-y-auto pr-1">
                  <div v-for="estancia in estanciasDesglose" :key="estancia.id" class="group">
                    <div
                      @click="toggleEstancia(estancia)"
                      :class="selectedEstanciaId === estancia.id ? 'border-emerald-500/50 bg-[#0d1320]' : 'border-white/5 bg-[#090d16] hover:border-white/10'"
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
                          :class="{ 'rotate-180 text-emerald-400': selectedEstanciaId === estancia.id }">
                          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                          </svg>
                        </span>
                      </div>
                    </div>

                    <div
                      v-if="selectedEstanciaId === estancia.id"
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
                        <p class="text-slate-500 text-xs italic">Sin dispositivos registrados en esta estancia.</p>
                      </div>
                    </div>
                  </div>

                  <p v-if="estanciasDesglose.length === 0" class="text-slate-500 text-xs text-center py-6">Sin registros.</p>
                </div>
              </div>

              <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col justify-between">
                <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest mb-2">Distribución</h2>
                <div class="relative h-44 w-full flex items-center justify-center">
                  <Doughnut :data="doughnutChartData" :options="doughnutChartOptions" />
                </div>
              </div>

              <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md flex flex-col justify-between">
                <div>
                  <h2 class="text-xs font-bold text-slate-400 uppercase tracking-widest mb-4">Recomendaciones IA</h2>
                  <div class="space-y-3">
                    <div v-for="(rec, index) in globalData.recomendaciones" :key="index" class="p-3.5 bg-[#090d16] border border-white/5 rounded-xl text-xs text-slate-300 leading-relaxed flex items-start space-x-2.5">
                      <span class="text-emerald-400 mt-0.5 font-bold">💡</span>
                      <p>{{ rec }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>

          <template v-else-if="currentTab === 'dispositivos'">
            <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md space-y-6">
              <div class="flex justify-between items-center">
                <div>
                  <h2 class="text-sm font-bold text-white uppercase tracking-wider">Gestión de Dispositivos</h2>
                  <p class="text-xs text-slate-400 mt-0.5">Administra las cargas eléctricas registradas en tu hogar u oficina.</p>
                </div>
                <button
                  @click="abrirModalCrearDispositivo"
                  class="px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20 flex items-center space-x-1.5"
                >
                  <span>+ Nuevo Dispositivo</span>
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
                        {{ disp.nombreVariante || disp.varianteEtiqueta || 'Estándar' }}
                        <span v-if="disp.potenciaWatts" class="text-emerald-400 text-[10px] ml-1">({{ disp.potenciaWatts }}W)</span>
                      </td>
                      <td class="py-3.5 px-4 text-slate-300 font-mono">
                        {{ disp.horasUsoDiarias }} hrs/día
                      </td>
                      <td class="py-3.5 px-4 text-emerald-400 font-mono font-bold">
                        {{ disp.consumoMensualKwh ?? disp.consumoKwh ?? 0 }} kWh
                      </td>
                      <td class="py-3.5 px-4 text-right">
                        <button
                          @click="eliminarDispositivo(disp.id || disp.dispositivoId)"
                          class="px-3 py-1.5 bg-rose-500/10 hover:bg-rose-500/20 text-rose-400 border border-rose-500/20 rounded-xl transition font-bold text-[11px]"
                          title="Remover dispositivo"
                        >
                          Eliminar
                        </button>
                      </td>
                    </tr>

                    <tr v-if="dispositivosRegistrados.length === 0 && !cargandoDispositivos">
                      <td colspan="6" class="text-center py-12 text-slate-500 italic">
                        No hay dispositivos registrados. ¡Añade el primero usando el botón superior!
                      </td>
                    </tr>

                    <tr v-if="cargandoDispositivos">
                      <td colspan="6" class="text-center py-12 text-slate-400">
                        Cargando dispositivos...
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </template>

          <template v-else-if="currentTab === 'reportes'">
            <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-6 shadow-xl backdrop-blur-md space-y-6">
              <div class="flex justify-between items-center">
                <div>
                  <h2 class="text-sm font-bold text-white uppercase tracking-wider">Historial de Reportes Energéticos</h2>
                  <p class="text-xs text-slate-400 mt-0.5">Consulta los análisis e inferencias guardados en la base de datos.</p>
                </div>
                <button
                  @click="generarNuevoReporte"
                  :disabled="generandoReporte"
                  class="px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20 flex items-center space-x-1.5 disabled:opacity-50"
                >
                  <span>⚡</span>
                  <span>{{ generandoReporte ? 'Generando...' : 'Generar Nuevo Reporte' }}</span>
                </button>
              </div>

              <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse">
                  <thead>
                    <tr class="border-b border-white/10 text-[10px] text-slate-400 uppercase tracking-widest">
                      <th class="py-3 px-4 font-bold">ID / Fecha</th>
                      <th class="py-3 px-4 font-bold">Consumo Total</th>
                      <th class="py-3 px-4 font-bold">Costo Estimado</th>
                      <th class="py-3 px-4 font-bold">Categoría IA</th>
                      <th class="py-3 px-4 font-bold">Probabilidad</th>
                    </tr>
                  </thead>
                  <tbody class="divide-y divide-white/5 text-xs">
                    <tr v-for="rep in historialReportes" :key="rep.id" class="hover:bg-white/[0.02] transition">
                      <td class="py-3.5 px-4 font-mono text-white">
                        <div class="font-bold">#{{ rep.id }}</div>
                        <div class="text-[10px] text-slate-400">{{ rep.fechaCreacion || rep.fecha_creacion || 'Fecha reciente' }}</div>
                      </td>
                      <td class="py-3.5 px-4 text-slate-300 font-mono font-bold">
                        {{ rep.consumoActual ?? rep.consumo_actual ?? 0 }} kWh
                      </td>
                      <td class="py-3.5 px-4 text-emerald-400 font-mono font-bold">
                        $ {{ Number(rep.costoEstimado ?? rep.costo_estimado ?? 0).toFixed(2) }}
                      </td>
                      <td class="py-3.5 px-4">
                        <span :class="getBadgeClassForCat(rep.categoria)" class="px-2.5 py-1 rounded-full border font-bold text-[10px] uppercase tracking-wider">
                          {{ rep.categoria || 'OPTIMIZADO' }}
                        </span>
                      </td>
                      <td class="py-3.5 px-4 text-slate-300 font-mono">
                        {{ rep.probabilidad !== undefined && rep.probabilidad !== null ? Number(rep.probabilidad).toFixed(2) : 'N/A' }}
                      </td>
                    </tr>

                    <tr v-if="historialReportes.length === 0 && !cargandoReportes">
                      <td colspan="5" class="text-center py-12 text-slate-500 italic">
                        No hay reportes previos registrados en el historial. ¡Haz clic en "Generar Nuevo Reporte"!
                      </td>
                    </tr>

                    <tr v-if="cargandoReportes">
                      <td colspan="5" class="text-center py-12 text-slate-400">
                        Cargando historial de reportes...
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </template>

          <template v-else-if="currentTab === 'graficos'">
            <div class="bg-[#121824]/80 border border-white/5 rounded-2xl p-8 shadow-xl backdrop-blur-md text-center py-16">
              <div class="text-4xl mb-3">📈</div>
              <h2 class="text-lg font-bold text-white uppercase tracking-wider">Tendencias Avanzadas</h2>
              <p class="text-xs text-slate-400 mt-1">Módulo para análisis histórico detallado y proyecciones de consumo.</p>
            </div>
          </template>
        </main>
      </div>
    </div>

    <div v-if="abrirModalDispositivo" class="fixed inset-0 z-[9999] bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 w-screen h-screen">
      <div class="w-full max-w-md bg-[#121824] border border-white/15 shadow-2xl p-6 rounded-2xl space-y-4 relative z-[10000]">
        <div class="flex justify-between items-center">
          <h3 class="text-sm font-bold text-white uppercase tracking-wider">Registrar Nuevo Dispositivo</h3>
          <button @click="abrirModalDispositivo = false" class="text-slate-400 hover:text-white text-sm font-bold">✕</button>
        </div>

        <form @submit.prevent="handleCrearDispositivo" class="space-y-4">
          <div>
            <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Seleccionar Estancia (DB)</label>
            <select
              v-model="nuevoDispositivoForm.estanciaId"
              required
              class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            >
              <option value="" disabled>Seleccione una estancia...</option>
              <option v-for="est in estanciasDisponibles" :key="est.id || est.estanciaId" :value="est.id || est.estanciaId">
                {{ est.nombre || est.nombreEstancia }}
              </option>
            </select>
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Tipo de Dispositivo / Catálogo (DB)</label>
            <select
              v-model="nuevoDispositivoForm.equipoId"
              required
              class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            >
              <option value="" disabled>Seleccione del catálogo...</option>
              <option v-for="tipo in tiposDispositivosDisponibles" :key="tipo.id || tipo.equipoId" :value="tipo.id || tipo.equipoId">
                {{ tipo.nombre || tipo.descripcion || tipo.nombreEquipo }}
              </option>
            </select>
          </div>

          <div v-if="tieneVariantesSeleccionado">
            <label class="block text-xs font-semibold text-emerald-400 mb-1 uppercase tracking-wider">Capacidad / Variante</label>
            <select
              v-model="nuevoDispositivoForm.varianteId"
              required
              class="w-full px-4 py-2.5 bg-[#090d16] border border-emerald-500/40 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            >
              <option value="" disabled>Seleccione una variante...</option>
              <option v-for="v in variantesDisponibles" :key="v.id || v.varianteId" :value="v.id || v.varianteId">
                {{ v.etiqueta }} ({{ v.potenciaWatts || v.potencia_watts }}W)
              </option>
            </select>
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Alias / Nombre Personalizado</label>
            <input
              type="text"
              v-model="nuevoDispositivoForm.alias"
              required
              placeholder="Ej. Aire Principal Habitación"
              class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            />
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Horas de Uso / Día</label>
            <input
              type="number"
              min="1"
              max="24"
              v-model.number="nuevoDispositivoForm.horasUsoDiarias"
              required
              class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
            />
          </div>

          <p v-if="dispositivoError" class="text-rose-400 text-xs text-center font-medium">{{ dispositivoError }}</p>

          <div class="flex justify-end space-x-3 pt-2">
            <button type="button" @click="abrirModalDispositivo = false" class="px-4 py-2 bg-white/5 hover:bg-white/10 text-slate-300 font-bold rounded-xl text-xs transition">Cancelar</button>
            <button type="submit" class="px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20">Guardar Dispositivo</button>
          </div>
        </form>
      </div>
    </div>

  </div>
</template>