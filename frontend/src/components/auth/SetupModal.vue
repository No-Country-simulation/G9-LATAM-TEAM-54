<script setup>
import { ref, watch } from "vue"

const props = defineProps({
  opcionesTemperatura: {
    type: Array,
    default: () => []
  },
  error: String
})

const emit = defineEmits(["submit"])

const form = ref({
  avgTemperatureC: null,
  householdSize: 2
})

watch(() => props.opcionesTemperatura, (ops) => {
  if (ops && ops.length > 0 && form.value.avgTemperatureC === null) {
    form.value.avgTemperatureC = ops[0].valorNumerico
  }
}, { immediate: true })

const handleSubmit = () => {
  emit("submit", { ...form.value })
}
</script>

<template>
  <div class="fixed inset-0 z-50 bg-[#090d16]/95 backdrop-blur-xl flex items-center justify-center p-4 w-full h-screen">
    <div class="w-full max-w-lg bg-[#121824] border border-emerald-500/40 shadow-2xl p-8 rounded-2xl space-y-6">
      <div class="text-center">
        <div class="inline-flex items-center justify-center w-12 h-12 rounded-2xl bg-emerald-500/10 text-emerald-400 mb-3 shadow-inner">
          <svg class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
          </svg>
        </div>
        <span class="px-3 py-1 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-[10px] font-bold uppercase tracking-widest">
          Configuración Requerida
        </span>
        <h2 class="text-2xl font-extrabold text-white tracking-tight mt-3">Personalicemos tu entorno</h2>
        <p class="text-xs text-slate-400 mt-1 max-w-sm mx-auto leading-relaxed">
          Para calibrar nuestros algoritmos de predicción y ofrecerte un cálculo de costos preciso, necesitamos conocer un par de detalles clave sobre tu espacio.
        </p>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-xs font-semibold text-slate-300 mb-1 uppercase tracking-wider">Temperatura Promedio de la Zona</label>
          <select
            v-model.number="form.avgTemperatureC"
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
            v-model.number="form.householdSize"
            required
            class="w-full px-4 py-3 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
          />
        </div>

        <p v-if="error" class="text-rose-400 text-xs text-center font-medium">{{ error }}</p>

        <button
          type="submit"
          class="w-full py-3.5 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-950 font-bold rounded-xl transition shadow-lg shadow-emerald-500/20 text-sm mt-2"
        >
          Guardar y Entrar al Dashboard
        </button>
      </form>
    </div>
  </div>
</template>
