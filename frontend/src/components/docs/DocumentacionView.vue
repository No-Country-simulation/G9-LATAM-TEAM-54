<script setup>
import { ref } from "vue"

const DOCS_URL = "https://energiai-g9-latam-team-54.vercel.app"
const isLoading = ref(true)
const iframeRef = ref(null)

const handleIframeLoad = () => {
  isLoading.value = false
}

const reloadIframe = () => {
  isLoading.value = true
  if (iframeRef.value) {
    iframeRef.value.src = DOCS_URL
  }
}
</script>

<template>
  <div class="space-y-4 flex flex-col h-[calc(100vh-140px)] min-h-[550px]">
    <!-- Top Bar with Actions & Info -->
    <div class="bg-[#121824]/90 border border-white/5 p-4 sm:p-5 rounded-2xl flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 shadow-xl backdrop-blur-xl shrink-0">
      <div class="flex items-center space-x-3">
        <div class="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 shrink-0 shadow-inner">
          <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
            <line x1="8" y1="6" x2="16" y2="6"/>
            <line x1="8" y1="10" x2="16" y2="10"/>
            <line x1="8" y1="14" x2="13" y2="14"/>
          </svg>
        </div>
        <div>
          <div class="flex items-center space-x-2">
            <h2 class="text-sm sm:text-base font-extrabold text-white tracking-tight">Documentación Oficial</h2>
            <span class="px-2 py-0.5 rounded-md bg-emerald-500/15 border border-emerald-500/30 text-[10px] font-mono font-bold text-emerald-400 uppercase">
              Vercel Live
            </span>
          </div>
          <p class="text-xs text-slate-400 mt-0.5">Bitácora de desarrollo, arquitectura y especificaciones del sistema</p>
        </div>
      </div>

      <div class="flex items-center space-x-2 w-full sm:w-auto justify-end">
        <button
          @click="reloadIframe"
          class="px-3 py-2 bg-white/5 hover:bg-white/10 text-slate-300 hover:text-white border border-white/10 rounded-xl transition text-xs font-semibold inline-flex items-center gap-1.5 cursor-pointer leading-none active:scale-[0.98]"
          title="Recargar documentación"
        >
          <svg class="w-3.5 h-3.5" :class="{ 'animate-spin': isLoading }" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21.5 2v6h-6M21.34 15.57a10 10 0 1 1-.57-8.38l5.67-5.67"/>
          </svg>
          <span class="hidden xs:inline">Recargar</span>
        </button>

        <a
          :href="DOCS_URL"
          target="_blank"
          rel="noopener noreferrer"
          class="px-3.5 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-extrabold rounded-xl transition-all duration-200 shadow-lg shadow-emerald-500/20 hover:shadow-emerald-500/35 text-xs inline-flex items-center gap-1.5 cursor-pointer leading-none active:scale-[0.98]"
        >
          <span>Abrir Externa</span>
          <svg class="w-3.5 h-3.5 text-slate-950" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path>
            <polyline points="15 3 21 3 21 9"></polyline>
            <line x1="10" y1="14" x2="21" y2="3"></line>
          </svg>
        </a>
      </div>
    </div>

    <!-- Embedded Iframe Container -->
    <div class="flex-1 bg-[#121824]/90 border border-white/5 rounded-2xl shadow-2xl backdrop-blur-xl relative overflow-hidden flex flex-col">
      <!-- Loading State -->
      <div
        v-if="isLoading"
        class="absolute inset-0 bg-[#090d16]/80 backdrop-blur-sm z-10 flex flex-col items-center justify-center space-y-3"
      >
        <div class="w-12 h-12 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 shadow-inner">
          <svg class="animate-spin h-6 w-6 text-emerald-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
          </svg>
        </div>
        <p class="text-xs font-bold text-slate-300">Cargando Documentación Oficial...</p>
        <p class="text-[11px] text-slate-500 font-mono">https://energiai-g9-latam-team-54.vercel.app</p>
      </div>

      <!-- Iframe -->
      <iframe
        ref="iframeRef"
        :src="DOCS_URL"
        class="w-full flex-1 border-0 rounded-2xl bg-white"
        title="Documentación Oficial EnergiAI"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
        sandbox="allow-same-origin allow-scripts allow-popups allow-forms allow-downloads"
        @load="handleIframeLoad"
      ></iframe>
    </div>
  </div>
</template>
