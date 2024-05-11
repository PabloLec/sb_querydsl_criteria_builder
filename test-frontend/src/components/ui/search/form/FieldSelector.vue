<template>
  <div :class="{ 'border-red-400 border-2 rounded-lg w-fit': !props.modelValue }">
    <Popover v-model:open="open">
      <PopoverTrigger as-child>
        <Button variant="outline" role="combobox" :aria-expanded="open" class="w-[200px] justify-between" :data-testid="dataTestid">
          {{ value ? availableFields?.find((field) => field.value === value)?.label : "Select field" }}
          <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent class="w-[200px] p-0" :data-testid="dataTestid + '-popover'">
        <Command v-model="value">
          <CommandInput placeholder="Search field" />
          <CommandEmpty>No field found.</CommandEmpty>
          <CommandList>
            <CommandGroup>
              <CommandItem
                v-for="field in availableFields"
                :key="field.value"
                :value="field.value"
                @select="open = false"
              >
                <Check :class="cn('mr-2 h-4 w-4', value === field.label ? 'opacity-100' : 'opacity-0')" />
                {{ field.label }}
              </CommandItem>
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, watchEffect } from "vue"
import { cn } from "@/lib/utils"
import { Check, ChevronsUpDown } from "lucide-vue-next"
import { Button } from "@/components/ui/shadcn/button"
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/shadcn/command"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/shadcn/popover"
import { fieldsConfiguration } from "@/lib/search/fieldsConfiguration.ts"

const props = defineProps({
  parentField: String,
  modelValue: String,
  dataTestid: String,
})

const open = ref(false)
const value = ref(props.modelValue)

const emit = defineEmits(["update:modelValue", "change"])

const currentFieldsConfig = computed(() => props.parentField && fieldsConfiguration[props.parentField])
const availableFields = computed(() => {
  if (!currentFieldsConfig.value || typeof currentFieldsConfig.value !== "object") {
    return undefined
  }

  return Object.entries(currentFieldsConfig.value).map(([key, value]) => {
    return { value: key, label: value.label }
  })
})

watchEffect(() => {
  emit("update:modelValue", value.value)
})
</script>
