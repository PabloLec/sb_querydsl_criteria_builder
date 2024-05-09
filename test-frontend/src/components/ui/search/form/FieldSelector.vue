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
import { computed } from 'vue';
import { fieldsConfiguration } from '@/lib/search/fieldsConfiguration.ts';

const props = defineProps({
  parentField: String
});

const emit = defineEmits(['update:modelValue', 'change']);

const currentFieldsConfig = computed(() => fieldsConfiguration[props.parentField]);

const handleChange = (value: string) => {
  emit('update:modelValue', value);
};
</script>
