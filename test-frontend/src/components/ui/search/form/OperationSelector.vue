<template>
  <Select @update:modelValue="handleChange">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="Select operation" />
    </SelectTrigger>
    <SelectContent>
      <SelectItem v-for="operator in operators" :key="operator.op" :value="operator.op">
        {{ operator.label }}
      </SelectItem>
    </SelectContent>
  </Select>
</template>

<script lang="ts" setup>
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/shadcn/select"
import { computed } from "vue"
import { fieldsConfiguration } from "@/lib/search/fieldsConfiguration"
import { fieldTypeToOperators } from "@/lib/search/operators"

const props = defineProps({
  field: String,
  parentField: String,
})

const emit = defineEmits(["update:modelValue", "change"])

const fieldConfig = computed(() => props.parentField && props.field && fieldsConfiguration[props.parentField][props.field])

const operators = computed(() => fieldConfig.value && fieldTypeToOperators[fieldConfig.value.fieldType] || [])

const handleChange = (value: string) => {
  emit("update:modelValue", value)
}
</script>
