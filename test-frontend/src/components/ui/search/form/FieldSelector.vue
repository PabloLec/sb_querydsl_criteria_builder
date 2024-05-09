<template>
  <Select @update:modelValue="handleChange">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="Field" />
    </SelectTrigger>
    <SelectContent>
      <SelectItem v-for="(config, field) in currentFieldsConfig" :key="field" :value="field">
        {{ config.label }}
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
} from '@/components/ui/shadcn/select';
import {computed, watch} from 'vue';
import { fieldsConfiguration } from '@/lib/search/fieldsConfiguration.ts';

const props = defineProps({
  parentField: String
});

const emit = defineEmits(['update:modelValue', 'change']);

const currentFieldsConfig = computed(() => fieldsConfiguration[props.parentField]);

const handleChange = (value: string | number) => {
  console.log("Value changed to:", value);
  emit('update:modelValue', value);
};

watch(() => props.modelValue, (newValue) => {
  console.log("New modelValue received:", newValue);
});

</script>