<template>
  <div>
    <div v-for="(criterion, index) in criteria" :key="index">
      <div class="flex items-center space-x-4 my-2">
        <field-selector v-model="criterion.field" @change="() => updateFieldConfig(criterion)" :parent-field="parentField" />
        <operation-selector v-if="hasOperations(criterion.field)" v-model="criterion.op" :field="criterion.field" />
        <value-input v-if="hasValueInput(criterion.field)" v-model="criterion.value" :field="criterion.field" />
        <Button size="icon" @click="removeCriterion(index)" class="bg-orange-400">
          <Minus class="w-4 h-4" />
        </Button>
      </div>
      <div v-if="hasSubCriteria(criterion)" class="my-2 ml-6">
        <search-form :criteria="criterion.subCriteria" :parent-field="criterion.field" />
        <Button v-if="canHaveSubCriteria(criterion.field)" @click="() => addSubCriterion(criterion)" class="bg-emerald-600">
          <Plus class="w-4 h-4" />
        </Button>
      </div>
    </div>
    <Button v-if="isRoot" size="icon" @click="addCriterion" class="bg-emerald-400">
      <Plus class="w-4 h-4" />
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

const props = defineProps({
  criteria: Array as PropType<SearchCriterion[]>,
  parentField: String,
  isRoot: {
    type: Boolean,
    default: false
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

const hasOperations = (field: string) => field && fieldsConfiguration[field]?.opOptions.length > 0;
const hasValueInput = (field: string) => field && fieldsConfiguration[field]?.valueComponent;
const canHaveSubCriteria = (field: string) => field && fieldsConfiguration[field]?.canHaveSubCriteria;
const hasSubCriteria = (criterion: SearchCriterion) => criterion.subCriteria && criterion.field && fieldsConfiguration[criterion.field]?.canHaveSubCriteria;
</script>
