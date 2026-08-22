<script setup>
import { ref } from "vue"

defineProps({
  error: String,
  success: String
})

const emit = defineEmits(["submit", "go-login"])

const form = ref({
  nombre: "",
  email: "",
  password: ""
})

const handleSubmit = () => {
  emit("submit", { ...form.value })
}
</script>

<template>
  <div class="flex-1 flex items-center justify-center p-4 sm:p-6 w-full">
    <div class="w-full max-w-md bg-[#121824]/90 backdrop-blur-xl border border-white/10 shadow-2xl p-5 sm:p-8 rounded-2xl animate-fade-in-up transition-all duration-300">
      <div class="text-center mb-6">
        <div class="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 mx-auto mb-3 shadow-inner">
          <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="8.5" cy="7" r="4"></circle>
            <line x1="20" y1="8" x2="20" y2="14"></line>
            <line x1="23" y1="11" x2="17" y2="11"></line>
          </svg>
        </div>
        <h2 class="text-xl sm:text-2xl font-extrabold text-white tracking-tight">Crea tu cuenta</h2>
        <p class="text-xs text-slate-400 mt-1">Conéctate a la plataforma inteligente EnergiAI</p>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Nombre Completo</label>
          <input
            type="text"
            v-model="form.nombre"
            required
            placeholder="Ej. Adrián Pérez"
            class="w-full px-4 py-3 sm:py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-base sm:text-sm transition shadow-inner placeholder:text-slate-600"
          />
        </div>

        <div>
          <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Correo Electrónico</label>
          <input
            type="email"
            v-model="form.email"
            required
            placeholder="correo@energia.com"
            class="w-full px-4 py-3 sm:py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-base sm:text-sm transition shadow-inner placeholder:text-slate-600"
          />
        </div>

        <div>
          <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Contraseña</label>
          <input
            type="password"
            v-model="form.password"
            required
            placeholder="••••••••"
            class="w-full px-4 py-3 sm:py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-base sm:text-sm transition shadow-inner placeholder:text-slate-600"
          />
        </div>

        <p v-if="error" class="text-rose-400 text-xs text-center font-medium">{{ error }}</p>
        <p v-if="success" class="text-emerald-400 text-xs text-center font-medium">{{ success }}</p>

        <button
          type="submit"
          class="w-full py-3.5 sm:py-3 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-extrabold rounded-xl transition-all duration-200 shadow-lg shadow-emerald-500/20 hover:shadow-emerald-500/35 active:scale-[0.98] text-sm sm:text-base mt-2 inline-flex items-center justify-center cursor-pointer leading-none"
        >
          Completar Registro
        </button>
      </form>

      <p class="text-xs text-center text-slate-400 mt-6">
        ¿Ya tienes una cuenta? <button @click="emit('go-login')" class="text-emerald-400 font-bold hover:underline cursor-pointer">Inicia sesión</button>
      </p>
    </div>
  </div>
</template>
