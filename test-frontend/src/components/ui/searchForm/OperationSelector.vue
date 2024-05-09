<template>
  <Select @update:modelValue="handleChange">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="op" />
    </SelectTrigger>
    <SelectContent>
      <SelectItem v-for="op in currentFieldsConfig[field]?.opOptions" :key="op" :value="op">
        {{ op }}
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
} from '@/components/ui/select';
import { computed } from 'vue';
import { fieldsConfiguration } from '@/lib/fieldsConfiguration';

const props = defineProps({
  field: String,
  parentField: String
});

const emit = defineEmits(['update:modelValue', 'change']);

const currentFieldsConfig = computed(() => fieldsConfiguration[props.parentField]);

const handleChange = (value: string) => {
  emit('update:modelValue', value);
};
</script>
