export interface FieldConfig {
  label: string
  fieldType: "string" | "number" | "date" | "datetime" | "subquery"
  valueComponent: "input" | "select" | "none"
  isFieldWithSubCriteria: boolean
  valueOptions?: string[]
}

export interface Operator {
  op: string
  label: string
}

export interface SearchCriterion {
  field?: string
  op?: string
  value?: string
  subQuery?: boolean
  subCriteria?: SearchCriterion[]
}

export interface Library {
  libraryId?: number
  name?: string
  location?: string
  openingHours?: string
  establishedDate?: string
  website?: string
  email?: string
  phoneNumber?: string
  isOpen?: boolean
}
