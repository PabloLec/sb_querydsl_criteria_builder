<template>
  <select :value="modelValue" @change="handleChange">
    <option disabled value="">Select a field</option>
    <option v-for="(config, field) in fieldsConfiguration" :key="field" :value="field">
      {{ field }}
    </option>
  </select>
</template>

<script lang="ts" setup>
import {fieldsConfiguration} from '@/lib/fieldsConfiguration';

const props = defineProps({
  modelValue: String
});

const emit = defineEmits(['update:modelValue', 'change']);

const handleChange = (event: Event) => {
  const target = event.target as HTMLSelectElement;
  emit('update:modelValue', target.value);
  emit('change');
};
</script>
