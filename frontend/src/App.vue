<script setup>
import { ref, onMounted, watch } from "vue"
import {
  Chart as ChartJS,
  CategoryScale, LinearScale, PointElement, LineElement,
  ArcElement, Tooltip, Legend, Title, Filler
} from "chart.js"

import apiClient from "./api/client.js"

import LandingView from "./components/auth/LandingView.vue"
import LoginForm from "./components/auth/LoginForm.vue"
import RegisterForm from "./components/auth/RegisterForm.vue"
import SetupModal from "./components/auth/SetupModal.vue"

import AppSidebar from "./components/layout/AppSidebar.vue"
import AppHeader from "./components/layout/AppHeader.vue"

import ToastNotification from "./components/common/ToastNotification.vue"
import ConfirmModal from "./components/common/ConfirmModal.vue"

import StatCards from "./components/dashboard/StatCards.vue"
import EnergyLineChart from "./components/dashboard/EnergyLineChart.vue"
import DistributionDonutChart from "./components/dashboard/DistributionDonutChart.vue"
import RecommendationsCard from "./components/dashboard/RecommendationsCard.vue"
import EstanciasAccordion from "./components/dashboard/EstanciasAccordion.vue"

import DispositivosView from "./components/devices/DispositivosView.vue"
import DeviceModal from "./components/devices/DeviceModal.vue"

import ReportesView from "./components/reports/ReportesView.vue"
import TendenciasView from "./components/trends/TendenciasView.vue"

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Tooltip, Legend, Title, Filler)

const isAuthenticated = ref(false)
const userName = ref("")
const currentTab = ref("dashboard")
const authView = ref("landing")
const needsSetup = ref(false)

const selectedEstanciaId = ref(null)

const modalDispositivoVisible = ref(false)
const estanciasDisponibles = ref([])
const tiposDispositivosDisponibles = ref([])
const variantesDisponibles = ref([])
const dispositivosRegistrados = ref([])
const cargandoDispositivos = ref(false)
const dispositivoError = ref("")

const historialReportes = ref([])
const cargandoReportes = ref(false)
const generandoReporte = ref(false)

const toastMessage = ref("")
const showToast = ref(false)
let toastTimeout = null

const modalConfirmDispositivo = ref({ visible: false, id: null, alias: "", cargando: false })
const modalConfirmReporte = ref({ visible: false, id: null, cargando: false })

const loginError = ref("")
const registerError = ref("")
const registerSuccess = ref("")
const setupError = ref("")
const opcionesTemperatura = ref([])

const globalData = ref({ consumoActual: 0, costoEstimado: 0.0, categoria: "OPTIMIZADO", recomendaciones: [] })
const estanciasDesglose = ref([])

const triggerToast = (msg) => {
  if (toastTimeout) clearTimeout(toastTimeout)
  toastMessage.value = msg
  showToast.value = true
  toastTimeout = setTimeout(() => { showToast.value = false }, 3500)
}

watch(currentTab, (newTab) => {
  if (newTab === "dispositivos") fetchMisDispositivos()
  else if (newTab === "reportes") fetchHistorialReportes()
})

const fetchOpcionesTemperatura = async () => {
  try {
    const res = await apiClient.get("/configuracion/opciones-temperatura")
    if (res.data) opcionesTemperatura.value = res.data
  } catch (e) { console.error(e) }
}

const fetchEstanciasDB = async () => {
  try {
    const res = await apiClient.get("/estancias")
    if (res.data) estanciasDisponibles.value = res.data
  } catch (e) { console.error(e) }
}

const fetchTiposDispositivosDB = async () => {
  try {
    const res = await apiClient.get("/catalogo")
    if (res.data) tiposDispositivosDisponibles.value = res.data
  } catch (e) { console.error(e) }
}

const fetchMisDispositivos = async () => {
  cargandoDispositivos.value = true
  try {
    const res = await apiClient.get("/dispositivos")
    if (res.data && Array.isArray(res.data)) dispositivosRegistrados.value = res.data
  } catch (e) { console.error(e) }
  finally { cargandoDispositivos.value = false }
}

const fetchHistorialReportes = async () => {
  cargandoReportes.value = true
  try {
    const res = await apiClient.get("/historial")
    if (res.data && Array.isArray(res.data)) historialReportes.value = res.data
  } catch {
    try {
      const fb = await apiClient.get("/analisis-energetico/historial")
      if (fb.data && Array.isArray(fb.data)) historialReportes.value = fb.data
    } catch (e) { console.error(e) }
  } finally { cargandoReportes.value = false }
}

const fetchDashboardData = async () => {
  try {
    const res = await apiClient.get("/dashboard/actual")
    if (res.data) {
      const d = res.data
      globalData.value.consumoActual = d.consumoTotal ?? 0
      globalData.value.costoEstimado = d.costoTotal ?? 0.0
      globalData.value.categoria = d.categoria ?? "OPTIMIZADO"
      globalData.value.recomendaciones = d.recomendaciones?.length > 0
        ? d.recomendaciones
        : ["El consumo se encuentra en un rango moderado y equilibrado."]
      estanciasDesglose.value = (d.desgloseEstancias || []).map(est => ({
        ...est,
        id: est.id || est.estanciaId,
        nombreEstancia: est.nombreEstancia || "Estancia",
        consumoKwh: est.consumoKwh ?? 0,
        costo: est.costo ?? 0.0,
        dispositivos: est.dispositivos || null
      }))
    }
  } catch (e) {
    console.error(e)
    if (e.response?.status === 403) logout()
  }
}

const fetchUserProfile = async () => {
  try {
    const res = await apiClient.get("/usuarios/me")
    if (res.data) {
      userName.value = res.data.nombre || res.data.name || "Usuario"
      const avgTemp = res.data.avgTemperatureC ?? res.data.avgTemperature ?? res.data.temperaturaPromedio
      const houseSize = res.data.householdSize ?? res.data.household_size ?? res.data.miembrosHogar
      if (!avgTemp || !houseSize) {
        needsSetup.value = true
        globalData.value = { consumoActual: 0, costoEstimado: 0.0, categoria: "", recomendaciones: [] }
        estanciasDesglose.value = []
        await fetchOpcionesTemperatura()
      } else {
        needsSetup.value = false
        await fetchDashboardData()
      }
    }
  } catch (e) { console.error(e) }
}

const handleLogin = async (formData) => {
  loginError.value = ""
  try {
    const res = await apiClient.post("/auth/login", { email: formData.email, password: formData.password })
    const token = res.data.token || res.data.accessToken || res.data.jwt || res.data
    localStorage.setItem("token", token)
    isAuthenticated.value = true
    await fetchUserProfile()
  } catch (e) {
    console.error(e)
    loginError.value = "Credenciales invalidas o error de conexion."
  }
}

const handleRegister = async (formData) => {
  registerError.value = ""
  registerSuccess.value = ""
  try {
    await apiClient.post("/usuarios", { nombre: formData.nombre, email: formData.email, password: formData.password })
    registerSuccess.value = "Cuenta creada con exito. Ahora puedes iniciar sesion."
    setTimeout(() => { authView.value = "login" }, 2000)
  } catch (e) {
    console.error(e)
    registerError.value = "No se pudo completar el registro."
  }
}

const handleSetupSubmit = async (formData) => {
  setupError.value = ""
  try {
    await apiClient.put("/usuarios/configuracion-inicial", { avgTemperatureC: formData.avgTemperatureC, householdSize: formData.householdSize })
    needsSetup.value = false
    await fetchDashboardData()
  } catch (e) {
    console.error(e)
    setupError.value = "No se pudo guardar la configuracion."
  }
}

const abrirModalCrearDispositivo = async () => {
  dispositivoError.value = ""
  await fetchEstanciasDB()
  await fetchTiposDispositivosDB()
  modalDispositivoVisible.value = true
}

const handleEquipoChange = async (equipoId) => {
  variantesDisponibles.value = []
  if (equipoId) {
    try {
      const res = await apiClient.get(`/catalogo/${equipoId}/variantes`)
      if (res.data && Array.isArray(res.data)) variantesDisponibles.value = res.data
    } catch (e) { console.error(e) }
  }
}

const handleCrearDispositivo = async (formData) => {
  dispositivoError.value = ""
  if (!formData.estanciaId) { dispositivoError.value = "Por favor selecciona una estancia."; return }
  if (!formData.equipoId) { dispositivoError.value = "Por favor selecciona un tipo de equipo."; return }
  if (formData.tieneVariantes && !formData.varianteId) { dispositivoError.value = "Por favor selecciona la capacidad / modelo."; return }
  if (!formData.alias?.trim()) { dispositivoError.value = "Por favor asigna un alias al dispositivo."; return }

  try {
    const payload = {
      estanciaId: Number(formData.estanciaId),
      equipoCatalogoId: Number(formData.equipoId),
      alias: formData.alias.trim(),
      horasUsoDiarias: Number(formData.horasUsoDiarias)
    }
    if (formData.tieneVariantes && formData.varianteId) {
      payload.equipoVarianteId = Number(formData.varianteId)
    }
    await apiClient.post("/dispositivos/guardar", payload)
    modalDispositivoVisible.value = false
    await fetchDashboardData()
    if (currentTab.value === "dispositivos") await fetchMisDispositivos()
    triggerToast("Dispositivo registrado exitosamente.")
  } catch (e) {
    console.error(e)
    dispositivoError.value = "No se pudo registrar el dispositivo."
  }
}

const confirmarEliminarDispositivo = (disp) => {
  modalConfirmDispositivo.value = { visible: true, id: disp.id || disp.dispositivoId, alias: disp.alias || "este dispositivo", cargando: false }
}

const ejecutarEliminarDispositivo = async () => {
  if (!modalConfirmDispositivo.value.id || modalConfirmDispositivo.value.cargando) return
  modalConfirmDispositivo.value.cargando = true
  try {
    await apiClient.delete(`/dispositivos/${modalConfirmDispositivo.value.id}`)
    modalConfirmDispositivo.value.visible = false
    await fetchMisDispositivos()
    await fetchDashboardData()
    triggerToast("Dispositivo eliminado exitosamente.")
  } catch (e) {
    console.error(e)
    triggerToast("No se pudo eliminar el dispositivo.")
  } finally { modalConfirmDispositivo.value.cargando = false }
}

const confirmarEliminarReporte = (rep) => {
  modalConfirmReporte.value = { visible: true, id: rep.id, cargando: false }
}

const ejecutarEliminarReporte = async () => {
  if (!modalConfirmReporte.value.id || modalConfirmReporte.value.cargando) return
  modalConfirmReporte.value.cargando = true
  try {
    await apiClient.delete(`/analisis/${modalConfirmReporte.value.id}`)
    const repId = modalConfirmReporte.value.id
    modalConfirmReporte.value.visible = false
    await fetchHistorialReportes()
    await fetchDashboardData()
    triggerToast(`Reporte #${repId} eliminado exitosamente.`)
  } catch (e) {
    console.error(e)
    triggerToast("No se pudo eliminar el reporte energetico.")
  } finally { modalConfirmReporte.value.cargando = false }
}

const generarNuevoReporte = async () => {
  if (generandoReporte.value) return
  generandoReporte.value = true
  try {
    await apiClient.post("/analisis-energetico")
    await fetchDashboardData()
    if (currentTab.value === "reportes") await fetchHistorialReportes()
    triggerToast("Reporte generado y guardado exitosamente.")
  } catch (e) {
    console.error(e)
    triggerToast("Error al generar el reporte energetico.")
  } finally { generandoReporte.value = false }
}

const toggleEstancia = async (estancia) => {
  const id = estancia.id || estancia.estanciaId
  if (selectedEstanciaId.value === id) {
    selectedEstanciaId.value = null
  } else {
    selectedEstanciaId.value = id
    if (!estancia.dispositivos) {
      try {
        const res = await apiClient.get(`/estancias/${id}/analisis`)
        if (res.data?.dispositivos) estancia.dispositivos = res.data.dispositivos
      } catch { estancia.dispositivos = [] }
    }
  }
}

const logout = () => {
  localStorage.removeItem("token")
  isAuthenticated.value = false
  userName.value = ""
  needsSetup.value = false
  authView.value = "landing"
  globalData.value = { consumoActual: 0, costoEstimado: 0.0, categoria: "OPTIMIZADO", recomendaciones: [] }
  estanciasDesglose.value = []
}

onMounted(() => {
  const savedToken = localStorage.getItem("token")
  if (savedToken) {
    isAuthenticated.value = true
    fetchUserProfile()
  }
})
</script>

<template>
  <div class="min-h-screen bg-[#0d111a] text-slate-100 flex selection:bg-emerald-500 selection:text-slate-950 font-sans relative overflow-x-hidden">

    <ToastNotification :show="showToast" :message="toastMessage" />

    <div v-if="!isAuthenticated" class="flex-1 flex flex-col justify-between bg-[#090d16] w-full">
      <header class="h-20 px-6 md:px-12 flex justify-between items-center border-b border-white/5 bg-[#090d16]/80 backdrop-blur-md sticky top-0 z-30">
        <div class="flex items-center space-x-3 cursor-pointer" @click="authView = 'landing'">
          <div class="w-9 h-9 rounded-xl bg-emerald-500 flex items-center justify-center font-black text-slate-950 shadow-lg shadow-emerald-500/30">E</div>
          <span class="text-lg font-extrabold text-white tracking-tight">Energi<span class="text-emerald-400">AI</span></span>
        </div>
        <div class="flex items-center space-x-2 sm:space-x-3">
          <button @click="authView = 'landing'" :class="authView === 'landing' ? 'text-white' : 'text-slate-400 hover:text-white'" class="text-xs transition font-semibold px-3 py-2 rounded-lg hover:bg-white/5 cursor-pointer leading-none inline-flex items-center justify-center">Inicio</button>
          <button @click="authView = 'login'" class="px-4 py-2 text-xs font-bold text-slate-200 hover:text-white bg-[#121824] hover:bg-white/5 border border-white/10 hover:border-emerald-500/30 rounded-xl transition shadow-sm active:scale-[0.98] inline-flex items-center justify-center cursor-pointer leading-none">Iniciar Sesión</button>
          <button @click="authView = 'register'" class="px-4 py-2 text-xs font-extrabold text-slate-950 bg-emerald-500 hover:bg-emerald-400 rounded-xl transition shadow-lg shadow-emerald-500/20 active:scale-[0.98] inline-flex items-center justify-center cursor-pointer leading-none">Registrarse</button>
        </div>
      </header>

      <LandingView v-if="authView === 'landing'" @go-login="authView = 'login'" @go-register="authView = 'register'" />
      <LoginForm v-else-if="authView === 'login'" :error="loginError" @submit="handleLogin" @go-register="authView = 'register'" />
      <RegisterForm v-else-if="authView === 'register'" :error="registerError" :success="registerSuccess" @submit="handleRegister" @go-login="authView = 'login'" />
    </div>

    <template v-else>
      <SetupModal v-if="needsSetup" :opciones-temperatura="opcionesTemperatura" :error="setupError" @submit="handleSetupSubmit" />

      <template v-if="!needsSetup">
        <AppSidebar :current-tab="currentTab" :user-name="userName" @update:tab="currentTab = $event" />

        <div class="flex-1 flex flex-col min-h-screen overflow-hidden">
          <AppHeader
            :current-tab="currentTab"
            :generando-reporte="generandoReporte"
            @generar-reporte="generarNuevoReporte"
            @logout="logout"
          />

          <main class="flex-1 overflow-y-auto p-6 md:p-8 space-y-6">

            <template v-if="currentTab === 'dashboard'">
              <StatCards
                :consumo-actual="globalData.consumoActual"
                :costo-estimado="globalData.costoEstimado"
                :categoria="globalData.categoria"
              />
              <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 items-stretch">
                <div class="lg:col-span-2 flex flex-col">
                  <EnergyLineChart />
                </div>
                <DistributionDonutChart :estancias-desglose="estanciasDesglose" />
              </div>
              <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 items-stretch">
                <div class="lg:col-span-2 flex flex-col">
                  <EstanciasAccordion
                    :estancias-desglose="estanciasDesglose"
                    :selected-estancia-id="selectedEstanciaId"
                    @toggle-estancia="toggleEstancia"
                    @abrir-modal-dispositivo="abrirModalCrearDispositivo"
                  />
                </div>
                <RecommendationsCard :recomendaciones="globalData.recomendaciones" />
              </div>
            </template>

            <DispositivosView
              v-else-if="currentTab === 'dispositivos'"
              :dispositivos-registrados="dispositivosRegistrados"
              :cargando-dispositivos="cargandoDispositivos"
              @abrir-modal="abrirModalCrearDispositivo"
              @eliminar="confirmarEliminarDispositivo"
            />

            <ReportesView
              v-else-if="currentTab === 'reportes'"
              :historial-reportes="historialReportes"
              :cargando-reportes="cargandoReportes"
              :generando-reporte="generandoReporte"
              @generar="generarNuevoReporte"
              @eliminar="confirmarEliminarReporte"
            />

            <TendenciasView v-else-if="currentTab === 'graficos'" />
          </main>
        </div>

        <DeviceModal
          :visible="modalDispositivoVisible"
          :estancias-disponibles="estanciasDisponibles"
          :tipos-dispositivos-disponibles="tiposDispositivosDisponibles"
          :variantes-disponibles="variantesDisponibles"
          :error="dispositivoError"
          @close="modalDispositivoVisible = false"
          @submit="handleCrearDispositivo"
          @equipo-change="handleEquipoChange"
        />

        <ConfirmModal
          :visible="modalConfirmDispositivo.visible"
          title="Eliminar Dispositivo"
          message="Esta accion eliminara permanentemente el dispositivo"
          :item-name="modalConfirmDispositivo.alias"
          :loading="modalConfirmDispositivo.cargando"
          @confirm="ejecutarEliminarDispositivo"
          @cancel="modalConfirmDispositivo.visible = false"
        />

        <ConfirmModal
          :visible="modalConfirmReporte.visible"
          title="Eliminar Reporte"
          message="Esta accion eliminara permanentemente el reporte energetico #"
          :item-name="String(modalConfirmReporte.id)"
          :loading="modalConfirmReporte.cargando"
          @confirm="ejecutarEliminarReporte"
          @cancel="modalConfirmReporte.visible = false"
        />
      </template>
    </template>
  </div>
</template>
