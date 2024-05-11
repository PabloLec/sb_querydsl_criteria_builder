<template>
  <div>
    <div v-for="(criterion, index) in criteria" :key="index" class="w-full">
      <div class="flex items-center space-x-4 my-2 w-full">
        <div class="flex flex-grow items-center space-x-4">
          <div class="flex-none">
            <field-selector
              v-model="criterion.field"
              @change="() => updateFieldConfig(criterion)"
              :parent-field="parentField"
            />
          </div>
          <div class="flex-none">
            <operation-selector
              v-if="criterion.field"
              v-model="criterion.op"
              :field="criterion.field"
              :parent-field="parentField"
            />
          </div>
          <div class="flex-grow">
            <value-input
              v-if="hasValueInput(criterion.field)"
              v-model="criterion.value"
              :field="criterion.field"
              :parent-field="parentField"
            />
          </div>
        </div>
        <Button
          variant="ghost"
          size="icon"
          @click="removeCriterion(index)"
          class="hover:text-destructive flex-shrink-0 w-10 h-10"
        >
          <CircleMinus class="w-4 h-4 flex-shrink-0" />
        </Button>
      </div>

      <div v-if="hasSubCriteria(criterion)" class="my-2 ml-6 relative">
        <div class="absolute left-0 top-0 bottom-0 w-0.5 bg-gray-400" style="margin-left: -1rem"></div>
        <search-form :criteria="criterion.subCriteria" :parent-field="criterion.field" :is-root="false" />
        <Button
          v-if="isFieldWithSubCriteria(criterion.field)"
          variant="outline"
          @click="() => addSubCriterion(criterion)"
          class="flex-shrink-0 w-8 h-8"
        >
          <Plus class="w-4 h-4 flex-shrink-0" />
        </Button>
      </div>
    </div>
    <Button v-if="isRoot" variant="outline" @click="addCriterion" class="flex-shrink-0 w-8 h-8">
      <Plus class="w-4 h-4 flex-shrink-0" />
    </Button>
    <Separator v-if="isRoot" class="m-4" />
    <div class="flex justify-end mt-4">
      <Button v-if="isRoot" @click="search" class="bg-blue-400 flex-shrink-0 mr-6"> Search </Button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, watch, PropType } from "vue"
import { useRoute, useRouter } from "vue-router"
import { Library, SearchCriterion } from "@/lib/search/types"
import { fieldsConfiguration } from "@/lib/search/fieldsConfiguration"
import { getLibrariesByQuery, filterCriteria } from "@/lib/api/client"
import FieldSelector from "./form/FieldSelector.vue"
import OperationSelector from "./form/OperationSelector.vue"
import ValueInput from "./form/ValueInput.vue"
import { CircleMinus, Plus } from "lucide-vue-next"
import { Button } from "@/components/ui/shadcn/button"
import { Separator } from "@/components/ui/shadcn/separator"

const emit = defineEmits(["update:criteria", "update:results"])

const route = useRoute()
const router = useRouter()
const props = defineProps({
  criteria: Array as PropType<SearchCriterion[]>,
  parentField: String,
  isRoot: {
    type: Boolean,
    default: true,
  },
})

const loadCriteriaFromURL = () => {
  if (!props.isRoot) {
    return
  }

  const queryCriteria = route.query.criteria
  if (queryCriteria) {
    try {
      const decodedCriteria = atob(queryCriteria as string)
      const initialCriteria = JSON.parse(decodedCriteria)
      if (props.criteria && Array.isArray(initialCriteria)) {
        props.criteria.splice(0, props.criteria.length, ...initialCriteria)
      }
    } catch (error) {
      console.error("Failed to parse criteria from URL:", error)
    }
  }
}

const updateCriteriaInURL = (newCriteria: SearchCriterion[]) => {
  if (!props.isRoot) {
    return
  }

  const criteriaString = JSON.stringify(filterCriteria(newCriteria))
  const encodedCriteria = btoa(criteriaString) // Encode en base64
  router.replace({ query: { criteria: encodedCriteria } }).catch((err) => {
    console.error("Failed to update URL:", err)
  })
}

onMounted(async () => {
  await router.isReady()
  loadCriteriaFromURL()
})

watch(
  props.criteria!!,
  (newCriteria) => {
    updateCriteriaInURL(newCriteria)
  },
  { deep: true }
)
const searchResults = ref<Library[]>([])

const currentFieldsConfig = computed(() => props.parentField && fieldsConfiguration[props.parentField])
const hasValueInput = (field: string | undefined) =>
  field && currentFieldsConfig.value && currentFieldsConfig.value[field]?.valueComponent
const isFieldWithSubCriteria = (field: string | undefined): boolean =>
  !!field && !!currentFieldsConfig.value && !!currentFieldsConfig.value[field]?.isFieldWithSubCriteria
const hasSubCriteria = (criterion: SearchCriterion) =>
  criterion.subCriteria &&
  criterion.field &&
  currentFieldsConfig.value &&
  currentFieldsConfig.value[criterion.field]?.isFieldWithSubCriteria

const addCriterion = () => {
  props.criteria?.push({ field: "", op: "", value: "", subCriteria: [] })
  emit("update:criteria", props.criteria)
}

const addSubCriterion = (criterion: SearchCriterion) => {
  if (!criterion.subCriteria) {
    criterion.subCriteria = []
  }
  criterion.subCriteria.push({ field: "", op: "", value: "", subCriteria: [] })
  emit("update:criteria", props.criteria)
}

const removeCriterion = (index: number) => {
  props.criteria?.splice(index, 1)
  emit("update:criteria", props.criteria)
}

const updateFieldConfig = (criterion: SearchCriterion) => {
  criterion.op = ""
  criterion.value = ""
  criterion.subQuery = isFieldWithSubCriteria(criterion.field)
  emit("update:criteria", props.criteria)
}

const search = async () => {
  try {
    if (!props.criteria) return
    searchResults.value = await getLibrariesByQuery(props.criteria)
    emit("update:results", searchResults.value)
  } catch (error) {
    console.error("Search failed", error)
  }
}
</script>
