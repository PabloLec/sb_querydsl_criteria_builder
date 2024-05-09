<template>
  <Input v-if="isInput" :modelValue="modelValue" type="text" placeholder="Value" @update:modelValue="handleChange" class="w-full"/>
  <Select v-if="isSelect" :modelValue="modelValue" @update:modelValue="handleChange">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="Value" />
    </SelectTrigger>
    <SelectContent>
      <SelectItem v-for="value in valueOptions" :key="value" :value="value">
        {{ value }}
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
} from '@/components/ui/shadcn/select'
import { Input } from '@/components/ui/shadcn/input'
import {computed} from 'vue';
import {fieldsConfiguration} from '@/lib/search/fieldsConfiguration.ts';

const props = defineProps({
  modelValue: [String, Number],
  field: String,
  parentField: String
});

const emit = defineEmits(['update:modelValue']);

const currentFieldsConfig = computed(() => fieldsConfiguration[props.parentField]);

const isInput = computed(() => currentFieldsConfig.value[props.field]?.valueComponent === 'input');
const isSelect = computed(() => currentFieldsConfig.value[props.field]?.valueComponent === 'select');
const valueOptions = computed(() => currentFieldsConfig.value[props.field]?.valueOptions ?? []);

const handleChange = (value: string | number) => {
  emit('update:modelValue', value);
};
</script>