<template>
  <div>
    <div v-for="(criterion, index) in criteria" :key="index">
      <field-selector v-model="criterion.field" @change="updateFieldConfig(criterion)" />
      <operation-selector v-if="criterion.field" v-model="criterion.op" :field="criterion.field" />
      <value-input v-if="criterion.field" v-model="criterion.value" :field="criterion.field" />
      <button @click="removeCriterion(index)">Remove</button>
      <button v-if="criterion.field && fieldsConfiguration[criterion.field]?.canHaveSubCriteria" @click="addSubCriterion(criterion)">Add Sub-Criterion</button>
      <div v-if="criterion.subCriteria && criterion.field && fieldsConfiguration[criterion.field]?.canHaveSubCriteria" style="margin-left: 20px;">
        <search-form :criteria="criterion.subCriteria"/>
      </div>
    </div>
    <button @click="addCriterion">Add Criterion</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { SearchCriterion } from '@/lib/types';
import { fieldsConfiguration } from '@/lib/fieldsConfiguration';
import FieldSelector from './FieldSelector.vue';
import OperationSelector from './OperationSelector.vue';
import ValueInput from './ValueInput.vue';

export default defineComponent({
  components: { FieldSelector, OperationSelector, ValueInput },
  props: {
    criteria: {
      type: Array as () => SearchCriterion[],
      default: () => []
    }
  },
  data() {
    return { fieldsConfiguration };
  },
  methods: {
    addCriterion() {
      this.criteria.push({ field: '', op: '', value: '', subCriteria: [] });
    },
    addSubCriterion(criterion: SearchCriterion) {
      if (!criterion.subCriteria) {
        criterion.subCriteria = [];
      }
      criterion.subCriteria.push({ field: '', op: '', value: '', subCriteria: [] });
    },
    removeCriterion(index: number) {
      this.criteria.splice(index, 1);
    },
    updateFieldConfig(criterion: SearchCriterion) {
      criterion.op = '';
      criterion.value = '';
    }
  }
});
</script>
