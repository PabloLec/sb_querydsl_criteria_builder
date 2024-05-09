<template>
  <div>
    <div v-for="(criterion, index) in criteria" :key="index">
      <select v-model="criterion.field" @change="updateFieldConfig(criterion)">
        <option disabled value="">Select a field</option>
        <option v-for="(config, field) in fieldsConfiguration" :key="field" :value="field">
          {{ field }}
        </option>
      </select>
      <select v-if="criterion.field && fieldsConfiguration[criterion.field]?.opOptions.length" v-model="criterion.op">
        <option v-for="op in fieldsConfiguration[criterion.field].opOptions" :key="op" :value="op">
          {{ op }}
        </option>
      </select>
      <input v-if="criterion.field && fieldsConfiguration[criterion.field]?.valueComponent === 'input'" v-model="criterion.value" type="text">
      <select v-if="criterion.field && fieldsConfiguration[criterion.field]?.valueComponent === 'select'" v-model="criterion.value">
        <option v-for="value in fieldsConfiguration[criterion.field].valueOptions" :key="value" :value="value">
          {{ value }}
        </option>
      </select>
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
import { SearchCriterion } from '@/lib/types'; // Assurez-vous que le chemin d'importation est correct
import { fieldsConfiguration } from '@/lib/fieldsConfiguration'; // Assurez-vous que le chemin d'importation est correct

export default defineComponent({
  name: 'SearchForm',
  props: {
    criteria: {
      type: Array as () => SearchCriterion[],
      default: () => []
    }
  },
  data() {
    return {
      fieldsConfiguration
    };
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
