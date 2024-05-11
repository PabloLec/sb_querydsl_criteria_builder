import { FieldConfig } from "./types.ts"

const libraryFieldsConfiguration: Record<string, FieldConfig> = {
  libraryId: {
    label: "ID",
    fieldType: "number",
    valueComponent: "input",
  },
  name: {
    label: "Name",
    fieldType: "string",
    valueComponent: "input",
  },
  location: {
    label: "Location",
    fieldType: "string",
    valueComponent: "input",
  },
  openingHours: {
    label: "Opening Hours",
    fieldType: "string",
    valueComponent: "input",
  },
  establishedDate: {
    label: "Established Date",
    fieldType: "date",
    valueComponent: "date",
  },
  website: {
    label: "Website",
    fieldType: "string",
    valueComponent: "input",
  },
  email: {
    label: "Email",
    fieldType: "string",
    valueComponent: "input",
  },
  phoneNumber: {
    label: "Phone Number",
    fieldType: "string",
    valueComponent: "input",
  },
  isOpen: {
    label: "Is Open",
    fieldType: "boolean",
    valueComponent: "select",
    valueOptions: ["Yes", "No"],
  },
  book: {
    label: "Book",
    fieldType: "subquery",
    valueComponent: "none",
  },
}

const bookFieldsConfiguration: Record<string, FieldConfig> = {
  bookId: {
    label: "ID",
    fieldType: "number",
    valueComponent: "input",
  },
  title: {
    label: "Title",
    fieldType: "string",
    valueComponent: "input",
  },
  isbn: {
    label: "ISBN",
    fieldType: "string",
    valueComponent: "input",
  },
  publishYear: {
    label: "Publish Year",
    fieldType: "number",
    valueComponent: "input",
  },
  edition: {
    label: "Edition",
    fieldType: "string",
    valueComponent: "input",
  },
  language: {
    label: "Language",
    fieldType: "enum",
    valueComponent: "select",
    valueOptions: [
      "English",
      "Spanish",
      "French",
      "German",
      "Italian",
      "Portuguese",
      "Russian",
      "Chinese",
      "Japanese",
      "Korean",
    ],
  },
  genre: {
    label: "Genre",
    fieldType: "enum",
    valueComponent: "select",
    valueOptions: [
      "Fiction",
      "Non-Fiction",
      "Fantasy",
      "Science Fiction",
      "Mystery",
      "Horror",
      "Romance",
      "Biography",
      "History",
      "Science",
      "Self-Help",
      "Cooking",
      "Travel",
      "Art",
      "Religion",
      "Philosophy",
      "Sports",
      "Music",
      "Technology",
      "Business",
      "Finance",
      "Politics",
      "Education",
      "Children",
      "Teen",
      "Young Adult",
    ],
  },
}

export const fieldsConfiguration: Record<string, Record<string, FieldConfig>> = {
  library: libraryFieldsConfiguration,
  book: bookFieldsConfiguration,
}
