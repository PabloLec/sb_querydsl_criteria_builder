<template>
  <div class="container py-10 mx-auto">
    <LibraryTable :columns="columns" :data="data" />
  </div>
</template>

<script lang="ts" setup>
import LibraryTable from "@/components/ui/results/table/LibraryTable.vue"
import { columns } from "@/lib/results/columns.ts"
import type { Library } from "@/lib/search/types.ts"

const props = defineProps<{
  data: Library[]
}>()
</script>
