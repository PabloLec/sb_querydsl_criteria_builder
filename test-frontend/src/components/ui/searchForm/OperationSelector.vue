<template>
  <select :value="modelValue" @change="handleChange">
    <option v-for="op in fieldsConfiguration[field].opOptions" :key="op" :value="op">
      {{ op }}
    </option>
  </select>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { fieldsConfiguration } from '@/lib/fieldsConfiguration';

export default defineComponent({
  props: {
    modelValue: String,
    field: String
  },
  data() {
    return { fieldsConfiguration };
  },
  methods: {
    handleChange(event) {
      this.$emit('update:modelValue', event.target.value);
    }
  },
  emits: ['update:modelValue']
});
</script>
