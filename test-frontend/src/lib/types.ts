export interface SearchCriterion {
    field?: string;
    op?: string;
    value?: string;
    subQuery?: boolean;
    subCriteria?: SearchCriterion[];
}

export interface FieldConfig {
    opOptions: string[];
    valueComponent: 'input' | 'select' | 'none';
    canHaveSubCriteria: boolean;
    valueOptions?: string[];
}
