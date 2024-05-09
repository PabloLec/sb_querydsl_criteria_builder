<script setup lang="ts">
import { ref, onMounted } from 'vue'
import SearchResults from '@/components/SearchResults.vue'
import SearchForm from '@/components/ui/searchForm/SearchForm.vue';
import type { SearchCriterion } from '@/lib/types';

const data = ref<Any[]>([])

const criteria = ref<SearchCriterion[]>([]);
onMounted(async () => {
  data.value = await getData();
});
</script>

<template>
  <div class="container py-10 mx-auto">
    <search-form :criteria="criteria"/>
  </div>
  <div class="container py-10 mx-auto">
    <SearchResults/>
  </div>
</template>
