<template>
  <div :class="{ 'border-red-400 border-2 rounded-lg': !props.modelValue }">
    <Select :modelValue="modelValue" @update:modelValue="handleChange">
      <SelectTrigger class="w-[180px]">
        <SelectValue placeholder="Select operation" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem v-for="operator in operators" :key="operator.op" :value="operator.op">
          {{ operator.label }}
        </SelectItem>
      </SelectContent>
    </Select>
  </div>
</template>

<script lang="ts" setup>
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/shadcn/select"
import { computed, watchEffect } from "vue"
import { fieldsConfiguration } from "@/lib/search/fieldsConfiguration"
import { fieldTypeToOperators } from "@/lib/search/operators"

const props = defineProps({
  field: String,
  parentField: String,
  modelValue: String,
})

const emit = defineEmits(["update:modelValue", "change"])

const fieldConfig = computed(
  () => props.parentField && props.field && fieldsConfiguration[props.parentField][props.field]
)

const operators = computed(
  () => (fieldConfig.value && fieldTypeToOperators[fieldConfig.value.fieldType]) || []
)

watchEffect(() => {
  // Set a default value for subQuery base field
  if (!props.modelValue && fieldConfig.value && fieldConfig.value.fieldType === "subquery") {
    emit("update:modelValue", operators.value[0].op)
  }
})

const handleChange = (value: string) => {
  emit("update:modelValue", value)
}
</script>
