<template>
  <Input v-if="isInput"  type="text" placeholder="Value" @update:modelValue="handleChange"/>
  <Select v-if="isSelect" @update:modelValue="handleChange">
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
} from '@/components/ui/select'
import { Input } from '@/components/ui/input'
import {computed} from 'vue';
import {fieldsConfiguration} from '@/lib/fieldsConfiguration';

const props = defineProps<{
  modelValue: string | number;
  field: string;
}>();

const emit = defineEmits(['update:modelValue']);

const isInput = computed(() => fieldsConfiguration[props.field]?.valueComponent === 'input');
const isSelect = computed(() => fieldsConfiguration[props.field]?.valueComponent === 'select');
const valueOptions = computed(() => fieldsConfiguration[props.field]?.valueOptions ?? []);

const handleChange = (value: string) => {
  emit('update:modelValue', value);
};
</script>
