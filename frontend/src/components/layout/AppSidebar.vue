<script setup>
defineProps({
  currentTab: String,
  userName: String
})

const emit = defineEmits(["update:tab"])
</script>

<template>
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
          @click="emit('update:tab', 'dashboard')"
          :class="currentTab === 'dashboard' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
          class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider"
        >
          <span>📊</span>
          <span class="uppercase">Dashboard</span>
        </button>

        <button
          @click="emit('update:tab', 'dispositivos')"
          :class="currentTab === 'dispositivos' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
          class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider"
        >
          <span>🔌</span>
          <span class="uppercase">Dispositivos</span>
        </button>

        <button
          @click="emit('update:tab', 'reportes')"
          :class="currentTab === 'reportes' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
          class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider"
        >
          <span>📋</span>
          <span class="uppercase">Reportes</span>
        </button>

        <button
          @click="emit('update:tab', 'graficos')"
          :class="currentTab === 'graficos' ? 'bg-emerald-500 text-slate-950 font-bold shadow-lg shadow-emerald-500/20' : 'text-slate-400 hover:bg-white/5 hover:text-white'"
          class="w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-xs transition text-left tracking-wider"
        >
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
</template>
