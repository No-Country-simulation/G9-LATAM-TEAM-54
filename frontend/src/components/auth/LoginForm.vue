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
    <div class="w-full max-w-md bg-[#121824]/90 backdrop-blur-md border border-white/15 shadow-2xl p-8 rounded-2xl">
      <div class="text-center mb-6">
        <h2 class="text-2xl font-extrabold text-white tracking-tight">Bienvenido de nuevo</h2>
        <p class="text-xs text-slate-400 mt-1">Ingresa tus credenciales para acceder al panel</p>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Correo Electrónico</label>
          <input
            type="email"
            v-model="form.email"
            required
            placeholder="admin@energia.com"
            class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
          />
        </div>

        <div>
          <label class="block text-xs font-semibold text-slate-400 mb-1 uppercase tracking-wider">Contraseña</label>
          <input
            type="password"
            v-model="form.password"
            required
            placeholder="••••••••"
            class="w-full px-4 py-2.5 bg-[#090d16] border border-white/10 rounded-xl text-white focus:outline-none focus:border-emerald-500 text-sm transition shadow-inner"
          />
        </div>

        <p v-if="error" class="text-rose-400 text-xs text-center font-medium">{{ error }}</p>

        <button
          type="submit"
          class="w-full py-3 bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-950 font-bold rounded-xl transition shadow-lg shadow-emerald-500/20 text-sm mt-2"
        >
          Iniciar Sesión
        </button>
      </form>

      <p class="text-xs text-center text-slate-400 mt-6">
        ¿No tienes cuenta? <button @click="emit('go-register')" class="text-emerald-400 font-bold hover:underline">Regístrate aquí</button>
      </p>
    </div>
  </div>
</template>
