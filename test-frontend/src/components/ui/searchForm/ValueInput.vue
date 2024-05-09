<template>
  <input v-if="fieldsConfiguration[field]?.valueComponent === 'input'" :value="modelValue" @input="handleInput" type="text" />
  <select v-if="fieldsConfiguration[field]?.valueComponent === 'select'" :value="modelValue" @change="handleChange">
    <option v-for="value in fieldsConfiguration[field].valueOptions" :key="value" :value="value">
      {{ value }}
    </option>
  </select>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { fieldsConfiguration } from '@/lib/fieldsConfiguration';

export default defineComponent({
  props: {
    modelValue: [String, Number],
    field: String
  },
  data() {
    return { fieldsConfiguration };
  },
  methods: {
    handleInput(event) {
      this.$emit('update:modelValue', event.target.value);
    },
    handleChange(event) {
      this.$emit('update:modelValue', event.target.value);
    }
  },
  emits: ['update:modelValue']
});
</script>
