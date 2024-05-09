<template>
  <select :value="modelValue" @change="handleChange">
    <option disabled value="">Select a field</option>
    <option v-for="(config, field) in fieldsConfiguration" :key="field" :value="field">
      {{ field }}
    </option>
  </select>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { fieldsConfiguration } from '@/lib/fieldsConfiguration';

export default defineComponent({
  props: {
    modelValue: String
  },
  data() {
    return { fieldsConfiguration };
  },
  methods: {
    handleChange(event) {
      this.$emit('update:modelValue', event.target.value);
      this.$emit('change');
    }
  },
  emits: ['update:modelValue', 'change']
});
</script>
