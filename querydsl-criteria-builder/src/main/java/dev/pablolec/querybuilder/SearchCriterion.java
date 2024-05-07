package dev.pablolec.querybuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchCriterion {
    private String field;
    private String op;
    private String value;
    private boolean subQuery;
    private List<SearchCriterion> subCriteria;

    public SearchCriterion(String field, String op, String value) {
        this.field = field;
        this.op = op;
        this.value = value;
        this.subQuery = false;
        this.subCriteria = null;
    }

    public SearchCriterion(String field, String op, List<SearchCriterion> subCriteria) {
        this.field = field;
        this.op = op;
        this.value = null;
        this.subQuery = true;
        this.subCriteria = subCriteria;
    }
}