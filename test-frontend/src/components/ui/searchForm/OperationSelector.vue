<template>
  <Select @update:modelValue="handleChange">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="op" />
    </SelectTrigger>
    <SelectContent>
      <SelectItem v-for="op in fieldsConfiguration[field].opOptions" :key="op" :value="op">
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
} from '@/components/ui/select'
import {fieldsConfiguration} from '@/lib/fieldsConfiguration';

const props = defineProps({
  field: String
});

const emit = defineEmits(['update:modelValue', 'change']);

const handleChange = (value: string) => {
  emit('update:modelValue', value);
};
</script>