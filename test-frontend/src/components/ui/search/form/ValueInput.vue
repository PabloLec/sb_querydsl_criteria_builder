<template>
  <div
    :class="{
      'border-red-400 border-2 rounded-lg w-full': !props.modelValue && isInput,
      'border-red-400 border-2 rounded-lg w-fit': !props.modelValue && hasValueField,
    }"
  >
    <Input
      v-if="isInput"
      :modelValue="modelValue"
      type="text"
      placeholder="Value"
      @update:modelValue="handleChange"
      class="w-full"
      :data-testid="props.dataTestid + '-input'"
    />

    <Select
      v-if="isSelect"
      :modelValue="stringModelValue"
      @update:modelValue="handleChange"
      :data-testid="props.dataTestid + '-select'"
    >
      <SelectTrigger class="w-[180px]">
        <SelectValue placeholder="Value" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem v-for="value in valueOptions" :key="value" :value="value">
          {{ value }}
        </SelectItem>
      </SelectContent>
    </Select>

    <Popover v-if="isDate">
      <PopoverTrigger as-child>
        <Button
          variant="outline"
          :class="cn('w-[280px] justify-start text-left font-normal', !dateValue && 'text-muted-foreground')"
          :data-testid="props.dataTestid + '-date-button'"
        >
          <CalendarIcon class="mr-2 h-4 w-4" />
          {{ dateValue ? df.format(dateValue.toDate(getLocalTimeZone())) : "Pick a date" }}
        </Button>
      </PopoverTrigger>
      <PopoverContent class="w-auto p-0">
        <Calendar v-model="dateValue" initial-focus :data-testid="props.dataTestid + '-date-picker'" />
      </PopoverContent>
    </Popover>
  </div>
</template>

<script lang="ts" setup>
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/shadcn/select"
import { DateFormatter, type DateValue, getLocalTimeZone, CalendarDate } from "@internationalized/date"

import { Calendar as CalendarIcon } from "lucide-vue-next"
import { Calendar } from "@/components/ui/shadcn/calendar"
import { Button } from "@/components/ui/shadcn/button"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/shadcn/popover"
import { cn } from "@/lib/utils"
import { Input } from "@/components/ui/shadcn/input"
import { computed, ref, watch, watchEffect } from "vue"
import { fieldsConfiguration } from "@/lib/search/fieldsConfiguration.ts"

const props = defineProps({
  modelValue: [String, Number],
  field: String,
  parentField: String,
  dataTestid: String,
})

const df = new DateFormatter("fr-FR", {
  dateStyle: "short",
})

const dateValue = ref<DateValue>()
const stringModelValue = computed(() => {
  return typeof props.modelValue === "string" ? props.modelValue : undefined
})

const emit = defineEmits(["update:modelValue"])

const currentFieldsConfig = computed(() => props.parentField && fieldsConfiguration[props.parentField])

const isInput = computed(
  () =>
    currentFieldsConfig.value &&
    props.field &&
    currentFieldsConfig.value[props.field]?.valueComponent === "input"
)
const isSelect = computed(
  () =>
    currentFieldsConfig.value &&
    props.field &&
    currentFieldsConfig.value[props.field]?.valueComponent === "select"
)
const isDate = computed(
  () =>
    currentFieldsConfig.value &&
    props.field &&
    currentFieldsConfig.value[props.field]?.valueComponent === "date"
)
const valueOptions = computed(
  () =>
    (currentFieldsConfig.value && props.field && currentFieldsConfig.value[props.field]?.valueOptions) ?? []
)

const hasValueField = computed(
  () =>
    currentFieldsConfig.value &&
    props.field &&
    !(currentFieldsConfig.value[props.field]?.fieldType === "subquery")
)

watch(dateValue, (newDate) => {
  if (newDate) {
    const formattedDate = `${newDate.year}-${String(newDate.month).padStart(2, "0")}-${String(newDate.day).padStart(2, "0")}`
    emit("update:modelValue", formattedDate)
  }
})

watchEffect(() => {
  if (isDate.value && props.modelValue && stringModelValue.value) {
    const [year, month, day] = stringModelValue.value.split("-").map(Number)
    dateValue.value = new CalendarDate(year, month, day)
  }
})

const handleChange = (value: string | number) => {
  emit("update:modelValue", value)
}
</script>
