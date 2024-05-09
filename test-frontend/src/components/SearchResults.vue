<script lang="ts" setup>
import {onMounted, ref} from 'vue'

import {columns} from "@/lib/search/columns"
import {components} from '@/lib/api-schema';
import {getLibrariesByQuery} from '@/lib/api';
import LibraryTable from "@/components/ui/LibraryTable.vue"

const data = ref<components['schemas']['Library'][]>([])

async function getData(): Promise<components['schemas']['Library'][]> {
  const query: components['schemas']['SearchCriterion'][] = [
    {
      field: "name",
      op: "like",
      value: "%Library%"
    }
  ];
  return getLibrariesByQuery(query)
}

onMounted(async () => {
  data.value = await getData();
});
</script>

<template>
  <div class="container py-10 mx-auto">
    <LibraryTable :columns="columns" :data="data"/>
  </div>
</template>