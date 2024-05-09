<template>
  <input v-if="isInput" :value="modelValue" type="text" @input="handleInput"/>
  <select v-if="isSelect" :value="modelValue" @change="handleChange">
    <option v-for="value in valueOptions" :key="value" :value="value">
      {{ value }}
    </option>
  </select>
</template>

<script lang="ts" setup>
import {computed} from 'vue';
import {fieldsConfiguration} from '@/lib/fieldsConfiguration';

const props = defineProps<{
  modelValue: string | number;
  field: string;
}>();

const emit = defineEmits(['update:modelValue']);

const isInput = computed(() => fieldsConfiguration[props.field]?.valueComponent === 'input');
const isSelect = computed(() => fieldsConfiguration[props.field]?.valueComponent === 'select');
const valueOptions = computed(() => fieldsConfiguration[props.field]?.valueOptions || []);

const handleInput = (event: Event) => {
  const target = event.target as HTMLInputElement;
  emit('update:modelValue', target.value);
};
const handleChange = (event: Event) => {
  const target = event.target as HTMLSelectElement;
  emit('update:modelValue', target.value);
};
</script>
