import { FieldConfig } from './types';

export const fieldsConfiguration: Record<string, FieldConfig> = {
    name: {
        opOptions: ['equals', 'contains'],
        valueComponent: 'input',
        canHaveSubCriteria: false
    },
    year: {
        opOptions: ['equals', 'greater than', 'less than'],
        valueComponent: 'input',
        canHaveSubCriteria: false
    },
    author: {
        opOptions: ['equals', 'not equals'],
        valueComponent: 'select',
        valueOptions: ['Author 1', 'Author 2', 'Author 3'],
        canHaveSubCriteria: false
    },
    book: {
        opOptions: [],
        valueComponent: 'none',
        canHaveSubCriteria: true
    },
    isOpen: {
        opOptions: ['is'],
        valueComponent: 'select',
        valueOptions: ['Open', 'Closed'],
        canHaveSubCriteria: false
    }
};
