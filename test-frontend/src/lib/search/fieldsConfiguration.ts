import { FieldConfig } from './types.ts';

const libraryFieldsConfiguration: Record<string, FieldConfig> = {
    libraryId: {
        label: "ID",
        fieldType: 'number',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    name: {
        label: "Name",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    location: {
        label: "Location",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    openingHours: {
        label: "Opening Hours",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    establishedDate: {
        label: "Established Date",
        fieldType: 'date',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    website: {
        label: "Website",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    email: {
        label: "Email",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    phoneNumber: {
        label: "Phone Number",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    isOpen: {
        label: "Is Open",
        fieldType: 'boolean',
        valueComponent: 'select',
        valueOptions: ['Yes', 'No'],
        isFieldWithSubCriteria: false
    },
    book: {
        label: "Book",
        fieldType: 'subquery',
        valueComponent: 'none',
        isFieldWithSubCriteria: true
    },
};

const bookFieldsConfiguration: Record<string, FieldConfig> = {
    bookId: {
        label: "ID",
        fieldType: 'number',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    title: {
        label: "Title",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    isbn: {
        label: "ISBN",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    publishYear: {
        label: "Publish Year",
        fieldType: 'number',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    edition: {
        label: "Edition",
        fieldType: 'string',
        valueComponent: 'input',
        isFieldWithSubCriteria: false
    },
    language: {
        label: "Language",
        fieldType: 'enum',
        valueComponent: 'select',
        valueOptions: ['English', 'Spanish', 'French', 'German', 'Italian', 'Portuguese', 'Russian', 'Chinese', 'Japanese', 'Korean'],
        isFieldWithSubCriteria: false
    },
    genre: {
        label: "Genre",
        fieldType: 'enum',
        valueComponent: 'select',
        valueOptions: ['Fiction', 'Non-Fiction', 'Fantasy', 'Science Fiction', 'Mystery', 'Horror', 'Romance', 'Biography', 'History', 'Science', 'Self-Help', 'Cooking', 'Travel', 'Art', 'Religion', 'Philosophy', 'Sports', 'Music', 'Technology', 'Business', 'Finance', 'Politics', 'Education', 'Children', 'Teen', 'Young Adult'],
        isFieldWithSubCriteria: false
    }
};

export const fieldsConfiguration: Record<string, Record<string, FieldConfig>> = {
    library: libraryFieldsConfiguration,
    book: bookFieldsConfiguration
};