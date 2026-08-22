<script setup>
import { computed } from "vue"

const props = defineProps({
  currentTab: String,
  generandoReporte: Boolean,
  isMobileMenuOpen: Boolean
})

const emit = defineEmits(["generar-reporte", "logout", "toggle-mobile-menu"])

const tabMeta = computed(() => {
  switch (props.currentTab) {
    case "dashboard":
      return { label: "Dashboard General", badge: "En tiempo real" }
    case "dispositivos":
      return { label: "Gestión de Dispositivos", badge: "Inventario" }
    case "reportes":
      return { label: "Historial de Reportes", badge: "Inferencia IA" }
    case "graficos":
      return { label: "Tendencias Avanzadas", badge: "Histórico" }
    case "documentacion":
      return { label: "Documentación Oficial", badge: "Bitácora Vercel" }
    default:
      return { label: "Panel", badge: "Activo" }
  }
})
</script>

<template>
  <header class="h-16 shrink-0 border-b border-white/5 px-4 sm:px-6 md:px-8 flex justify-between items-center bg-[#090d16]/90 backdrop-blur-xl z-20 sticky top-0 select-none">
    <div class="flex items-center space-x-2 sm:space-x-3 min-w-0 max-w-[60%] sm:max-w-none">
      <!-- Hamburger Menu Button (Mobile Only) -->
      <button
        @click="emit('toggle-mobile-menu')"
        class="p-2 -ml-1.5 rounded-xl text-slate-400 hover:text-white hover:bg-white/5 md:hidden transition-colors cursor-pointer focus:outline-none focus:ring-1 focus:ring-emerald-500/30 shrink-0"
        aria-label="Alternar menú"
      >
        <svg v-if="!isMobileMenuOpen" class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="3" y1="12" x2="21" y2="12"></line>
          <line x1="3" y1="6" x2="21" y2="6"></line>
          <line x1="3" y1="18" x2="21" y2="18"></line>
        </svg>
        <svg v-else class="w-5 h-5 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="18" y1="6" x2="6" y2="18"></line>
          <line x1="6" y1="6" x2="18" y2="18"></line>
        </svg>
      </button>

      <div class="w-8 h-8 rounded-lg bg-white/5 border border-white/10 flex items-center justify-center text-slate-300 shadow-inner shrink-0 hidden xs:flex">
        <svg v-if="currentTab === 'dashboard'" class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="3" y="3" width="7" height="9" rx="1"/>
          <rect x="14" y="3" width="7" height="5" rx="1"/>
          <rect x="14" y="12" width="7" height="9" rx="1"/>
          <rect x="3" y="16" width="7" height="5" rx="1"/>
        </svg>
        <svg v-else-if="currentTab === 'dispositivos'" class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 22v-5"/>
          <path d="M9 8V2"/>
          <path d="M15 8V2"/>
          <path d="M18 8v5a6 6 0 0 1-12 0V8z"/>
        </svg>
        <svg v-else-if="currentTab === 'reportes'" class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
          <polyline points="14 2 14 8 20 8"/>
          <line x1="16" y1="13" x2="8" y2="13"/>
          <line x1="16" y1="17" x2="8" y2="17"/>
        </svg>
        <svg v-else-if="currentTab === 'graficos'" class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
          <polyline points="17 6 23 6 23 12"/>
        </svg>
        <svg v-else-if="currentTab === 'documentacion'" class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
          <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
          <line x1="8" y1="6" x2="16" y2="6"/>
          <line x1="8" y1="10" x2="16" y2="10"/>
          <line x1="8" y1="14" x2="13" y2="14"/>
        </svg>
        <svg v-else class="w-4 h-4 text-emerald-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
        </svg>
      </div>

      <div class="min-w-0 overflow-hidden">
        <div class="flex items-center space-x-1.5 sm:space-x-2">
          <span class="text-[9px] sm:text-[10px] font-extrabold uppercase tracking-widest text-slate-400 truncate">EnergiAI</span>
          <span class="text-slate-600 text-xs">/</span>
          <h1 class="text-[11px] sm:text-xs font-bold uppercase tracking-wider text-white truncate">
            {{ tabMeta.label }}
          </h1>
        </div>
        <div class="flex items-center space-x-1.5 mt-0.5">
          <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse shrink-0"></span>
          <span class="text-[9px] sm:text-[10px] text-emerald-400/90 font-medium truncate">{{ tabMeta.badge }}</span>
        </div>
      </div>
    </div>

    <div class="flex items-center space-x-2 sm:space-x-3 shrink-0">
      <button
        v-if="currentTab === 'dashboard'"
        @click="emit('generar-reporte')"
        :disabled="generandoReporte"
        class="group relative px-3 sm:px-4 py-2 bg-emerald-500 hover:bg-emerald-400 disabled:bg-emerald-500/50 text-slate-950 rounded-xl font-extrabold text-xs transition-all duration-200 shadow-lg shadow-emerald-500/20 hover:shadow-emerald-500/35 active:scale-[0.98] inline-flex items-center justify-center gap-1.5 sm:gap-2 cursor-pointer disabled:cursor-not-allowed leading-none"
      >
        <svg v-if="!generandoReporte" class="w-3.5 h-3.5 text-slate-950 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
          <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
        </svg>
        <svg v-else class="animate-spin h-3.5 w-3.5 text-slate-950 shrink-0" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
        </svg>
        <span class="hidden md:inline">{{ generandoReporte ? "Generando..." : "Generar Reporte" }}</span>
        <span class="md:hidden">{{ generandoReporte ? "..." : "Reporte" }}</span>
      </button>

      <div class="h-6 w-[1px] bg-white/10 hidden sm:block"></div>

      <button
        @click="emit('logout')"
        class="px-2.5 sm:px-3 py-1.5 rounded-xl bg-white/5 hover:bg-rose-500/10 border border-white/5 hover:border-rose-500/20 text-slate-400 hover:text-rose-400 text-xs font-semibold transition-all duration-200 inline-flex items-center justify-center gap-1.5 cursor-pointer leading-none"
        title="Cerrar sesión"
      >
        <svg class="w-3.5 h-3.5 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
          <polyline points="16 17 21 12 16 7"></polyline>
          <line x1="21" y1="12" x2="9" y2="12"></line>
        </svg>
        <span class="hidden sm:inline">Salir</span>
      </button>
    </div>
  </header>
</template>
