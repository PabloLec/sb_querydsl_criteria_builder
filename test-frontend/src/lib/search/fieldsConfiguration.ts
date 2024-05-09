import { FieldConfig } from './types.ts';

const libraryFieldsConfiguration: Record<string, FieldConfig> = {
    name: {
        opOptions: ['equals', 'like'],
        valueComponent: 'input',
        canHaveSubCriteria: false
    },
    year: {
        opOptions: ['equals', 'greater than', 'less than'],
        valueComponent: 'input',
        canHaveSubCriteria: false
    },
    book: {
        opOptions: [],
        valueComponent: 'none',
        canHaveSubCriteria: true
    },
    author: {
        opOptions: ['equals', 'not equals'],
        valueComponent: 'select',
        valueOptions: ['Author 1', 'Author 2', 'Author 3'],
        canHaveSubCriteria: false
    },
    isOpen: {
        opOptions: ['is'],
        valueComponent: 'select',
        valueOptions: ['Open', 'Closed'],
        canHaveSubCriteria: false
    }
};

const bookFieldsConfiguration: Record<string, FieldConfig> = {
    author: {
        opOptions: ['equals', 'not equals'],
        valueComponent: 'select',
        valueOptions: ['Author 1', 'Author 2', 'Author 3', 'Author 4'],
        canHaveSubCriteria: false
    },
    genre: {
        opOptions: ['equals'],
        valueComponent: 'select',
        valueOptions: ['Fiction', 'Non-Fiction', 'Educational'],
        canHaveSubCriteria: false
    },
    language: {
        opOptions: ['equals'],
        valueComponent: 'select',
        valueOptions: ['English', 'French', 'Spanish'],
        canHaveSubCriteria: false
    },
    book: {
        opOptions: [],
        valueComponent: 'none',
        canHaveSubCriteria: true
    },
};

export const fieldsConfiguration: Record<string, Record<string, FieldConfig>> = {
    library: libraryFieldsConfiguration,
    book: bookFieldsConfiguration
};