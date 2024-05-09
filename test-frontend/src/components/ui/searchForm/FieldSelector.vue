<template>
  <Select @update:modelValue="handleChange">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="Field" />
    </SelectTrigger>
    <SelectContent>
      <SelectItem v-for="(config, field) in fieldsConfiguration" :key="field" :value="field">
        {{ field }}
      </SelectItem>
    </SelectContent>
  </Select>
</template>

<script lang="ts" setup>
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import {fieldsConfiguration} from '@/lib/fieldsConfiguration';

const emit = defineEmits(['update:modelValue', 'change']);

const handleChange = (value: string) => {
  emit('update:modelValue', value);
};
</script>