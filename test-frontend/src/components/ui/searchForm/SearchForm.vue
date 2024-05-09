<template>
  <div>
    <div v-for="(criterion, index) in criteria" :key="index">
      <div class="flex items-center space-x-4">
        <field-selector v-model="criterion.field" @change="() => updateFieldConfig(criterion)" />
        <operation-selector v-if="hasOperations(criterion.field)" v-model="criterion.op" :field="criterion.field" />
        <value-input v-if="hasValueInput(criterion.field)" v-model="criterion.value" :field="criterion.field" />
        <button @click="removeCriterion(index)" class="mt-2">Remove</button>
      </div>
      <button v-if="canHaveSubCriteria(criterion.field)" @click="() => addSubCriterion(criterion)" class="mt-2 ml-2">
        Add Sub-Criterion
      </button>
      <div v-if="hasSubCriteria(criterion)" class="mt-2 ml-6">
        <search-form :criteria="criterion.subCriteria"/>
      </div>
    </div>
    <button @click="addCriterion" class="mt-4">Add Criterion</button>
  </div>
</template>


<script lang="ts" setup>
import {SearchCriterion} from '@/lib/types';
import {fieldsConfiguration} from '@/lib/fieldsConfiguration';
import FieldSelector from './FieldSelector.vue';
import OperationSelector from './OperationSelector.vue';
import ValueInput from './ValueInput.vue';

const props = defineProps<{
  criteria: SearchCriterion[]
}>();

const emit = defineEmits(['update:criteria']);

const addCriterion = () => {
  props.criteria.push({field: '', op: '', value: '', subCriteria: []});
  emit('update:criteria', props.criteria);
};

const addSubCriterion = (criterion: SearchCriterion) => {
  if (!criterion.subCriteria) {
    criterion.subCriteria = [];
  }
  criterion.subCriteria.push({field: '', op: '', value: '', subCriteria: []});
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
