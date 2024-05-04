package dev.pablolec.backend.service.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriterion {
    private String field;
    private String op;
    private String value;
    private boolean subQuery;
    private SearchCriterion subCriterion;

    public SearchCriterion(String field, String op, String value) {
        this.field = field;
        this.op = op;
        this.value = value;
        this.subQuery = false;
        this.subCriterion = null;
    }

    public SearchCriterion(String field, String op, SearchCriterion subCriterion) {
        this.field = field;
        this.op = op;
        this.value = null;
        this.subQuery = true;
        this.subCriterion = subCriterion;
    }
}