export interface FieldConfig {
    opOptions: string[];
    valueComponent: 'input' | 'select' | 'none';
    canHaveSubCriteria: boolean;
    valueOptions?: string[];
}

export interface SearchCriterion {
    field?: string;
    op?: string;
    value?: string;
    subQuery?: boolean;
    subCriteria?: SearchCriterion[];
}

export interface Library {
    libraryId?: number;
    name?: string;
    location?: string;
    openingHours?: string;
    establishedDate?: string;
    website?: string;
    email?: string;
    phoneNumber?: string;
    isOpen?: boolean;
}