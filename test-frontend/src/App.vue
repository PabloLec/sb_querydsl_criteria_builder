<template>
  <div class="container py-10 mx-auto">
    <search-form :criteria="criteria" :is-root="true" :parent-field="'library'" @update:results="handleSearchResults"/>
  </div>
  <div class="container py-10 mx-auto">
    <SearchResults :data="searchResultsData" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import SearchResults from '@/components/ui/results/SearchResults.vue';
import SearchForm from '@/components/ui/search/SearchForm.vue';
import type { SearchCriterion, Library } from '@/lib/search/types.ts';

const criteria = ref<SearchCriterion[]>([]);
const searchResultsData = ref<Library[]>([]);

const handleSearchResults = (results: Library[]) => {
  searchResultsData.value = results;
};
</script>
