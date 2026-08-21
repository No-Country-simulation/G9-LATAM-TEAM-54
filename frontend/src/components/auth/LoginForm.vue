<script setup>
import { ref } from "vue"

defineProps({
  error: String
})

const emit = defineEmits(["submit", "go-register"])

const form = ref({
  email: "",
  password: ""
})

const handleSubmit = () => {
  emit("submit", { ...form.value })
}
</script>

<template>
  <div class="flex-1 flex items-center justify-center p-4">
    <div class="w-full max-w-md bg-[#121824]/90 backdrop-blur-xl border border-white/10 shadow-2xl p-8 rounded-2xl animate-fade-in-up">
      <div class="text-center mb-6">
        <div class="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 mx-auto mb-3 shadow-inner">
          <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
          </svg>
        </div>
        <h2 class="text-2xl font-extrabold text-white tracking-tight">Bienvenido de nuevo</h2>
        <p class="text-xs text-slate-400 mt-1">Ingresa tus credenciales para acceder al panel</p>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Correo Electrónico</label>
          <input
            type="email"
            v-model="form.email"
            required
            placeholder="admin@energia.com"
            class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner placeholder:text-slate-600"
          />
        </div>

        <div>
          <label class="block text-xs font-semibold text-slate-300 mb-1.5 uppercase tracking-wider">Contraseña</label>
          <input
            type="password"
            v-model="form.password"
            required
            placeholder="••••••••"
            class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner placeholder:text-slate-600"
          />
        </div>

        <p v-if="error" class="text-rose-400 text-xs text-center font-medium">{{ error }}</p>

        <button
          type="submit"
          class="w-full py-3 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-extrabold rounded-xl transition-all duration-200 shadow-lg shadow-emerald-500/20 hover:shadow-emerald-500/35 active:scale-[0.98] text-sm mt-2 inline-flex items-center justify-center cursor-pointer leading-none"
        >
          Iniciar Sesión
        </button>
      </form>

      <p class="text-xs text-center text-slate-400 mt-6">
        ¿No tienes cuenta? <button @click="emit('go-register')" class="text-emerald-400 font-bold hover:underline cursor-pointer">Regístrate aquí</button>
      </p>
    </div>
  </div>
</template>
