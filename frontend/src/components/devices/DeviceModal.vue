<script setup>
import { ref, computed, watch } from "vue"

const props = defineProps({
  visible: Boolean,
  estanciasDisponibles: { type: Array, default: () => [] },
  tiposDispositivosDisponibles: { type: Array, default: () => [] },
  variantesDisponibles: { type: Array, default: () => [] },
  error: String
})

const emit = defineEmits(["close", "submit", "equipo-change"])

const form = ref({
  estanciaId: "",
  equipoId: "",
  varianteId: "",
  alias: "",
  horasUsoDiarias: 4
})

const dropdownsAbiertos = ref({ estancia: false, equipo: false, variante: false })

const toggleDropdown = (tipo) => {
  for (const k in dropdownsAbiertos.value) {
    if (k !== tipo) dropdownsAbiertos.value[k] = false
  }
  dropdownsAbiertos.value[tipo] = !dropdownsAbiertos.value[tipo]
}

const cerrarTodos = () => {
  dropdownsAbiertos.value.estancia = false
  dropdownsAbiertos.value.equipo = false
  dropdownsAbiertos.value.variante = false
}

const estanciaObj = computed(() => props.estanciasDisponibles.find(e => (e.id || e.estanciaId) === form.value.estanciaId))
const equipoObj = computed(() => props.tiposDispositivosDisponibles.find(t => (t.id || t.equipoId) === form.value.equipoId))
const varianteObj = computed(() => props.variantesDisponibles.find(v => (v.id || v.varianteId) === form.value.varianteId))

const tieneVariantes = computed(() => {
  const item = equipoObj.value
  const val = item?.tieneVariantes ?? item?.tiene_variantes
  return val === 1 || val === true || val === "1"
})

watch(() => props.visible, (val) => {
  if (val) {
    form.value = { estanciaId: "", equipoId: "", varianteId: "", alias: "", horasUsoDiarias: 4 }
    cerrarTodos()
  }
})

const handleSubmit = () => {
  emit("submit", { ...form.value, tieneVariantes: tieneVariantes.value })
}
</script>

<template>
  <transition
    enter-active-class="transition duration-300 ease-out"
    enter-from-class="opacity-0"
    enter-to-class="opacity-100"
    leave-active-class="transition duration-200 ease-in"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="visible"
      class="fixed inset-0 z-[9999] bg-black/75 backdrop-blur-md flex items-center justify-center p-4 w-screen h-screen"
      @click.self="emit('close')"
    >
      <transition
        enter-active-class="transition duration-300 ease-out transform"
        enter-from-class="opacity-0 scale-95 translate-y-3"
        enter-to-class="opacity-100 scale-100 translate-y-0"
        appear
      >
        <div
          class="w-full max-w-lg bg-[#121824] border border-emerald-500/20 shadow-2xl p-6 sm:p-7 rounded-2xl space-y-5 backdrop-blur-xl relative z-[10000]"
          @click="cerrarTodos"
        >
          <div class="flex justify-between items-center pb-3 border-b border-white/5">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 text-lg">
                ⚡
              </div>
              <div>
                <h3 class="text-base font-extrabold text-white tracking-tight">Registrar Nuevo Dispositivo</h3>
                <p class="text-[11px] text-slate-400">Añade un equipo para calcular tu consumo energetico</p>
              </div>
            </div>
            <button
              type="button"
              @click="emit('close')"
              class="w-8 h-8 rounded-xl bg-white/5 hover:bg-white/10 text-slate-400 hover:text-white flex items-center justify-center text-xs transition font-bold"
            >
              X
            </button>
          </div>

          <form @submit.prevent="handleSubmit" class="space-y-4">
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div class="relative" @click.stop>
                <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Estancia</label>
                <button
                  type="button"
                  @click="toggleDropdown('estancia')"
                  :class="dropdownsAbiertos.estancia ? 'border-emerald-500 ring-1 ring-emerald-500/30' : 'border-white/10 hover:border-white/20'"
                  class="w-full flex items-center justify-between px-4 py-2.5 bg-[#090d16] border rounded-xl text-sm transition text-left cursor-pointer"
                >
                  <span :class="estanciaObj ? 'text-white font-medium truncate' : 'text-slate-500 truncate'">
                    {{ estanciaObj ? (estanciaObj.nombre || estanciaObj.nombreEstancia) : "Seleccione estancia..." }}
                  </span>
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-400 shrink-0 ml-2 transition-transform duration-200" :class="{ 'rotate-180 text-emerald-400': dropdownsAbiertos.estancia }" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </button>
                <transition enter-active-class="transition duration-150 ease-out" enter-from-class="opacity-0 scale-95 -translate-y-1" enter-to-class="opacity-100 scale-100 translate-y-0">
                  <div v-if="dropdownsAbiertos.estancia" class="absolute left-0 right-0 top-full mt-1.5 z-50 bg-[#0c121e] border border-white/15 rounded-xl shadow-2xl p-1.5 backdrop-blur-2xl max-h-52 overflow-y-auto space-y-1">
                    <div
                      v-for="est in estanciasDisponibles"
                      :key="est.id || est.estanciaId"
                      @click="form.estanciaId = est.id || est.estanciaId; dropdownsAbiertos.estancia = false"
                      :class="form.estanciaId === (est.id || est.estanciaId) ? 'bg-emerald-500/15 text-emerald-400 font-bold' : 'text-slate-300 hover:bg-white/5 hover:text-white'"
                      class="flex items-center justify-between px-3 py-2 rounded-lg text-xs transition cursor-pointer"
                    >
                      <span class="truncate">{{ est.nombre || est.nombreEstancia }}</span>
                      <span v-if="form.estanciaId === (est.id || est.estanciaId)" class="text-emerald-400 text-xs shrink-0 ml-2">✓</span>
                    </div>
                    <div v-if="estanciasDisponibles.length === 0" class="px-3 py-2 text-xs text-slate-500 italic text-center">Sin estancias</div>
                  </div>
                </transition>
              </div>

              <div class="relative" @click.stop>
                <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Tipo de Equipo</label>
                <button
                  type="button"
                  @click="toggleDropdown('equipo')"
                  :class="dropdownsAbiertos.equipo ? 'border-emerald-500 ring-1 ring-emerald-500/30' : 'border-white/10 hover:border-white/20'"
                  class="w-full flex items-center justify-between px-4 py-2.5 bg-[#090d16] border rounded-xl text-sm transition text-left cursor-pointer"
                >
                  <span :class="equipoObj ? 'text-white font-medium truncate' : 'text-slate-500 truncate'">
                    {{ equipoObj ? (equipoObj.nombre || equipoObj.descripcion || equipoObj.nombreEquipo) : "Seleccione equipo..." }}
                  </span>
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-400 shrink-0 ml-2 transition-transform duration-200" :class="{ 'rotate-180 text-emerald-400': dropdownsAbiertos.equipo }" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </button>
                <transition enter-active-class="transition duration-150 ease-out" enter-from-class="opacity-0 scale-95 -translate-y-1" enter-to-class="opacity-100 scale-100 translate-y-0">
                  <div v-if="dropdownsAbiertos.equipo" class="absolute left-0 right-0 top-full mt-1.5 z-50 bg-[#0c121e] border border-white/15 rounded-xl shadow-2xl p-1.5 backdrop-blur-2xl max-h-52 overflow-y-auto space-y-1">
                    <div
                      v-for="tipo in tiposDispositivosDisponibles"
                      :key="tipo.id || tipo.equipoId"
                      @click="form.equipoId = tipo.id || tipo.equipoId; dropdownsAbiertos.equipo = false; emit('equipo-change', tipo.id || tipo.equipoId)"
                      :class="form.equipoId === (tipo.id || tipo.equipoId) ? 'bg-emerald-500/15 text-emerald-400 font-bold' : 'text-slate-300 hover:bg-white/5 hover:text-white'"
                      class="flex items-center justify-between px-3 py-2 rounded-lg text-xs transition cursor-pointer"
                    >
                      <span class="truncate">{{ tipo.nombre || tipo.descripcion || tipo.nombreEquipo }}</span>
                      <span v-if="form.equipoId === (tipo.id || tipo.equipoId)" class="text-emerald-400 text-xs shrink-0 ml-2">✓</span>
                    </div>
                    <div v-if="tiposDispositivosDisponibles.length === 0" class="px-3 py-2 text-xs text-slate-500 italic text-center">Cargando...</div>
                  </div>
                </transition>
              </div>
            </div>

            <div v-if="tieneVariantes" class="relative" @click.stop>
              <label class="block text-xs font-semibold text-emerald-400 mb-1.5 uppercase tracking-wider">Capacidad / Modelo</label>
              <button
                type="button"
                @click="toggleDropdown('variante')"
                :class="dropdownsAbiertos.variante ? 'border-emerald-500 ring-1 ring-emerald-500/30' : 'border-emerald-500/40 hover:border-emerald-500/60'"
                class="w-full flex items-center justify-between px-4 py-2.5 bg-[#090d16] border rounded-xl text-sm transition text-left cursor-pointer"
              >
                <span :class="varianteObj ? 'text-emerald-300 font-medium truncate' : 'text-slate-500 truncate'">
                  {{ varianteObj ? `${varianteObj.etiqueta} (${varianteObj.potenciaWatts || varianteObj.potencia_watts}W)` : "Seleccione una variante..." }}
                </span>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-emerald-400 shrink-0 ml-2 transition-transform duration-200" :class="{ 'rotate-180': dropdownsAbiertos.variante }" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              <transition enter-active-class="transition duration-150 ease-out" enter-from-class="opacity-0 scale-95 -translate-y-1" enter-to-class="opacity-100 scale-100 translate-y-0">
                <div v-if="dropdownsAbiertos.variante" class="absolute left-0 right-0 top-full mt-1.5 z-50 bg-[#0c121e] border border-emerald-500/30 rounded-xl shadow-2xl p-1.5 backdrop-blur-2xl max-h-52 overflow-y-auto space-y-1">
                  <div
                    v-for="v in variantesDisponibles"
                    :key="v.id || v.varianteId"
                    @click="form.varianteId = v.id || v.varianteId; dropdownsAbiertos.variante = false"
                    :class="form.varianteId === (v.id || v.varianteId) ? 'bg-emerald-500/15 text-emerald-400 font-bold' : 'text-slate-300 hover:bg-white/5 hover:text-white'"
                    class="flex items-center justify-between px-3 py-2 rounded-lg text-xs transition cursor-pointer"
                  >
                    <span class="truncate">{{ v.etiqueta }} <span class="text-emerald-400/80 font-mono">({{ v.potenciaWatts || v.potencia_watts }}W)</span></span>
                    <span v-if="form.varianteId === (v.id || v.varianteId)" class="text-emerald-400 text-xs shrink-0 ml-2">✓</span>
                  </div>
                  <div v-if="variantesDisponibles.length === 0" class="px-3 py-2 text-xs text-slate-500 italic text-center">Sin variantes</div>
                </div>
              </transition>
            </div>

            <div>
              <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Alias del Dispositivo</label>
              <input
                type="text"
                v-model="form.alias"
                required
                placeholder="Ej. Aire Principal Habitacion"
                class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner placeholder:text-slate-600"
              />
            </div>

            <div class="bg-[#090d16]/70 border border-white/5 p-4 rounded-xl space-y-3">
              <div class="flex justify-between items-center">
                <label class="text-xs font-semibold text-slate-300 uppercase tracking-wider">Uso Diario Estimado</label>
                <span class="px-2.5 py-0.5 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 font-mono font-bold text-xs">
                  {{ form.horasUsoDiarias }} hrs / dia
                </span>
              </div>
              <input
                type="range" min="1" max="24" step="1"
                v-model.number="form.horasUsoDiarias"
                class="w-full h-1.5 bg-slate-800 rounded-lg appearance-none cursor-pointer accent-emerald-500"
              />
              <div class="flex justify-between items-center pt-1">
                <button
                  v-for="h in [2, 4, 8, 12, 24]" :key="h" type="button"
                  @click="form.horasUsoDiarias = h"
                  :class="form.horasUsoDiarias === h ? 'bg-emerald-500 text-slate-950 font-bold border-emerald-500 shadow' : 'bg-white/5 text-slate-400 hover:text-white border-white/5'"
                  class="px-2.5 py-1 rounded-lg border transition text-[11px] font-mono"
                >
                  {{ h }}h
                </button>
              </div>
            </div>

            <p v-if="error" class="text-rose-400 text-xs text-center font-medium">{{ error }}</p>

            <div class="flex items-center justify-end space-x-3 pt-2 border-t border-white/5">
              <button type="button" @click="emit('close')" class="px-5 py-2.5 bg-white/5 hover:bg-white/10 border border-white/10 text-slate-300 font-bold rounded-xl text-xs transition">
                Cancelar
              </button>
              <button type="submit" class="px-6 py-2.5 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-400 hover:to-teal-400 text-slate-950 font-extrabold rounded-xl text-xs transition shadow-lg shadow-emerald-500/20 flex items-center space-x-1.5">
                <span>Guardar Dispositivo</span>
              </button>
            </div>
          </form>
        </div>
      </transition>
    </div>
  </transition>
</template>
