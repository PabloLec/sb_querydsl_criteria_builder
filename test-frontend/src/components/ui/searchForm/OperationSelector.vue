<template>
  <select :value="modelValue" @change="handleChange">
    <option v-for="op in fieldsConfiguration[field].opOptions" :key="op" :value="op">
      {{ op }}
    </option>
  </select>
</template>

<script lang="ts" setup>
import {fieldsConfiguration} from '@/lib/fieldsConfiguration';

const props = defineProps({
  modelValue: String,
  field: String
});

const emit = defineEmits(['update:modelValue']);

const handleChange = (event: Event) => {
  const target = event.target as HTMLSelectElement;
  emit('update:modelValue', target.value);
};
</script>
