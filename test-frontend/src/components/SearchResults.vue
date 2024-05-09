<template>
  <div class="container py-10 mx-auto">
    <LibraryTable :columns="columns" :data="data"/>
  </div>
</template>

<script lang="ts" setup>
import LibraryTable from '@/components/ui/LibraryTable.vue';
import { columns } from "@/lib/search/columns";
import type { Library } from '@/lib/types';

const props = defineProps<{
  data: Library[]
}>();
</script>
