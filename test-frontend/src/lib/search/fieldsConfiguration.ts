import { FieldConfig } from './types.ts';

const libraryFieldsConfiguration: Record<string, FieldConfig> = {
    name: {
        label: "Name",
        fieldType: 'string',
        valueComponent: 'input',
        canHaveSubCriteria: false
    },
    year: {
        label: "Year",
        fieldType: 'number',
        valueComponent: 'input',
        canHaveSubCriteria: false
    },
    book: {
        label: "Book",
        fieldType: 'subquery',
        valueComponent: 'none',
        canHaveSubCriteria: true
    },
    author: {
        label: "Author",
        fieldType: 'enum',
        valueComponent: 'select',
        valueOptions: ['Author 1', 'Author 2', 'Author 3'],
        canHaveSubCriteria: false
    },
    isOpen: {
        label: "Is Open",
        fieldType: 'boolean',
        valueComponent: 'select',
        valueOptions: ['0', '1'],
        canHaveSubCriteria: false
    }
};

const bookFieldsConfiguration: Record<string, FieldConfig> = {
    author: {
        label: "Author",
        fieldType: 'enum',
        valueComponent: 'select',
        valueOptions: ['Author 1', 'Author 2', 'Author 3', 'Author 4'],
        canHaveSubCriteria: false
    },
    genre: {
        label: "Genre",
        fieldType: 'enum',
        valueComponent: 'select',
        valueOptions: ['Fiction', 'Non-Fiction', 'Educational'],
        canHaveSubCriteria: false
    },
    language: {
        label: "Language",
        fieldType: 'enum',
        valueComponent: 'select',
        valueOptions: ['English', 'French', 'Spanish'],
        canHaveSubCriteria: false
    },
    book: {
        label: "Book",
        fieldType: 'subquery',
        valueComponent: 'none',
        canHaveSubCriteria: true
    },
};

export const fieldsConfiguration: Record<string, Record<string, FieldConfig>> = {
    library: libraryFieldsConfiguration,
    book: bookFieldsConfiguration
};