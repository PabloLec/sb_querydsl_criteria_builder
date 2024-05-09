<template>
  <div>
    <div v-for="(criterion, index) in criteria" :key="index">
      <div class="flex items-center justify-between space-x-4 my-2">
        <div class="flex flex-grow items-center space-x-4">
          <field-selector v-model="criterion.field" @change="() => updateFieldConfig(criterion)" :parent-field="parentField" />
          <operation-selector v-if="hasOperations(criterion.field)" v-model="criterion.op" :field="criterion.field" :parent-field="parentField" />
          <value-input v-if="hasValueInput(criterion.field)" v-model="criterion.value" :field="criterion.field" :parent-field="parentField" />
        </div>
        <Button size="icon" @click="removeCriterion(index)" class="bg-orange-400 flex-shrink-0 w-10 h-10">
          <Minus class="w-4 h-4 flex-shrink-0" />
        </Button>
      </div>
      <div v-if="hasSubCriteria(criterion)" class="my-2 ml-6 relative">
        <div class="absolute left-0 top-0 bottom-0 w-0.5 bg-gray-400" style="margin-left: -1rem;"></div>
        <search-form :criteria="criterion.subCriteria" :parent-field="criterion.field" :is-root="false"/>
        <Button v-if="canHaveSubCriteria(criterion.field)" @click="() => addSubCriterion(criterion)" class="bg-emerald-600 flex-shrink-0 w-10 h-10">
          <Plus class="w-4 h-4 flex-shrink-0" />
        </Button>
      </div>
    </div>
    <Button v-if="isRoot" size="icon" @click="addCriterion" class="bg-emerald-400 flex-shrink-0 w-10 h-10">
      <Plus class="w-4 h-4 flex-shrink-0" />
    </Button>
  </div>
</template>

<script lang="ts" setup>
import { SearchCriterion } from '@/lib/types';
import { fieldsConfiguration } from '@/lib/fieldsConfiguration';
import FieldSelector from './FieldSelector.vue';
import OperationSelector from './OperationSelector.vue';
import ValueInput from './ValueInput.vue';
import { Minus, Plus } from 'lucide-vue-next';
import { Button } from '@/components/ui/button';
import {computed} from "vue";

const props = defineProps({
  criteria: Array as PropType<SearchCriterion[]>,
  parentField: String,
  isRoot: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['update:criteria']);

const addCriterion = () => {
  props.criteria.push({ field: '', op: '', value: '', subCriteria: [] });
  emit('update:criteria', props.criteria);
};

const addSubCriterion = (criterion: SearchCriterion) => {
  if (!criterion.subCriteria) {
    criterion.subCriteria = [];
  }
  criterion.subCriteria.push({ field: '', op: '', value: '', subCriteria: [] });
  emit('update:criteria', props.criteria);
};

const removeCriterion = (index: number) => {
  props.criteria.splice(index, 1);
  emit('update:criteria', props.criteria);
};

const updateFieldConfig = (criterion: SearchCriterion) => {
  criterion.op = '';
  criterion.value = '';
  emit('update:criteria', props.criteria);
};

const currentFieldsConfig = computed(() => fieldsConfiguration[props.parentField]);

const hasOperations = (field: string) => field && currentFieldsConfig.value[field]?.opOptions.length > 0;
const hasValueInput = (field: string) => field && currentFieldsConfig.value[field]?.valueComponent;
const canHaveSubCriteria = (field: string) => field && currentFieldsConfig.value[field]?.canHaveSubCriteria;
const hasSubCriteria = (criterion: SearchCriterion) => criterion.subCriteria && criterion.field && currentFieldsConfig.value[criterion.field]?.canHaveSubCriteria;
</script>
