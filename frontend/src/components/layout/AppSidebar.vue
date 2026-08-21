<script setup>
defineProps({
  currentTab: String,
  userName: String
})

const emit = defineEmits(["update:tab"])
</script>

<template>
  <aside class="w-full md:w-64 bg-[#090d16] border-r border-white/5 flex flex-col justify-between p-5 shrink-0 shadow-2xl h-screen sticky top-0 z-30 select-none">
    <div>
      <!-- Brand Header -->
      <div class="flex items-center space-x-3 mb-8 px-2 py-1">
        <div class="w-8 h-8 rounded-xl bg-emerald-500 flex items-center justify-center font-black text-slate-950 shadow-md shadow-emerald-500/30 text-sm">
          E
        </div>
        <div>
          <h2 class="font-extrabold text-white tracking-wider text-xs uppercase">Energi<span class="text-emerald-400">AI</span> Panel</h2>
          <span class="text-[9px] text-slate-400 font-bold tracking-widest uppercase">Eco Dashboard v2.0</span>
        </div>
      </div>

      <!-- Navigation -->
      <nav class="space-y-1.5">
        <button
          @click="emit('update:tab', 'dashboard')"
          :class="currentTab === 'dashboard' ? 'bg-emerald-500 text-slate-950 font-black shadow-lg shadow-emerald-500/25' : 'text-slate-400 hover:bg-white/5 hover:text-white font-semibold'"
          class="w-full flex items-center space-x-3 px-3.5 py-2.5 rounded-xl text-xs transition-all duration-200 text-left tracking-wider cursor-pointer group select-none"
        >
          <svg class="w-4 h-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="3" width="7" height="9" rx="1"/>
            <rect x="14" y="3" width="7" height="5" rx="1"/>
            <rect x="14" y="12" width="7" height="9" rx="1"/>
            <rect x="3" y="16" width="7" height="5" rx="1"/>
          </svg>
          <span class="uppercase">Dashboard</span>
        </button>

        <button
          @click="emit('update:tab', 'dispositivos')"
          :class="currentTab === 'dispositivos' ? 'bg-emerald-500 text-slate-950 font-black shadow-lg shadow-emerald-500/25' : 'text-slate-400 hover:bg-white/5 hover:text-white font-semibold'"
          class="w-full flex items-center space-x-3 px-3.5 py-2.5 rounded-xl text-xs transition-all duration-200 text-left tracking-wider cursor-pointer group select-none"
        >
          <svg class="w-4 h-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 22v-5"/>
            <path d="M9 8V2"/>
            <path d="M15 8V2"/>
            <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
          </svg>
          <span class="uppercase">Dispositivos</span>
        </button>

        <button
          @click="emit('update:tab', 'reportes')"
          :class="currentTab === 'reportes' ? 'bg-emerald-500 text-slate-950 font-black shadow-lg shadow-emerald-500/25' : 'text-slate-400 hover:bg-white/5 hover:text-white font-semibold'"
          class="w-full flex items-center space-x-3 px-3.5 py-2.5 rounded-xl text-xs transition-all duration-200 text-left tracking-wider cursor-pointer group select-none"
        >
          <svg class="w-4 h-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/>
            <line x1="16" y1="13" x2="8" y2="13"/>
            <line x1="16" y1="17" x2="8" y2="17"/>
            <polyline points="10 9 9 9 8 9"/>
          </svg>
          <span class="uppercase">Reportes</span>
        </button>

        <button
          @click="emit('update:tab', 'graficos')"
          :class="currentTab === 'graficos' ? 'bg-emerald-500 text-slate-950 font-black shadow-lg shadow-emerald-500/25' : 'text-slate-400 hover:bg-white/5 hover:text-white font-semibold'"
          class="w-full flex items-center space-x-3 px-3.5 py-2.5 rounded-xl text-xs transition-all duration-200 text-left tracking-wider cursor-pointer group select-none"
        >
          <svg class="w-4 h-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
            <polyline points="17 6 23 6 23 12"/>
          </svg>
          <span class="uppercase">Tendencias</span>
        </button>
      </nav>
    </div>

    <!-- User Profile Card -->
    <div class="pt-4 border-t border-white/5">
      <div class="flex items-center space-x-3 px-3 py-2 rounded-xl bg-white/[0.02] border border-white/5">
        <div class="w-8 h-8 rounded-full bg-emerald-500/15 border border-emerald-500/30 flex items-center justify-center text-emerald-400 font-extrabold text-xs shrink-0 shadow-inner">
          {{ userName ? userName.charAt(0).toUpperCase() : 'A' }}
        </div>
        <div class="overflow-hidden min-w-0">
          <p class="text-xs text-white font-bold truncate">{{ userName || 'Usuario' }}</p>
          <div class="flex items-center space-x-1.5 mt-0.5">
            <span class="w-1.5 h-1.5 rounded-full bg-emerald-400"></span>
            <span class="text-[9px] text-emerald-400/90 font-bold uppercase tracking-wider">Activo</span>
          </div>
        </div>
      </div>
    </div>
  </aside>
</template>
