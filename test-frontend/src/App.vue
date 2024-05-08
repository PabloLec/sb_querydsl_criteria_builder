<script setup lang="ts">
import { ref, onMounted } from 'vue'
import SearchResults from './components/SearchResults.vue'

const data = ref<Any[]>([])

async function getData(): Promise<Any[]> {
  // Fetch data from API here.
}

onMounted(async () => {
  data.value = await getData();
});
</script>

<template>
  <div class="container py-10 mx-auto">
    <SearchResults/>
  </div>
</template>
