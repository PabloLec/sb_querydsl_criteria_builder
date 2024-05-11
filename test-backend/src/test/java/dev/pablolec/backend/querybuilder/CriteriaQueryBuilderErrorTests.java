package dev.pablolec.backend.querybuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.pablolec.backend.AbstractIntegrationTest;
import dev.pablolec.backend.db.model.Library;
import dev.pablolec.querybuilder.CriteriaQueryBuilder;
import dev.pablolec.querybuilder.model.SearchCriterion;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class CriteriaQueryBuilderErrorTests extends AbstractIntegrationTest {
    @Autowired
    private CriteriaQueryBuilder criteriaQueryBuilder;

    @Test
    void testMissingEntityPath() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("name", "eq", "NonExistent"));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            criteriaQueryBuilder
                    .buildQuery(criteria, CriteriaQueryBuilderErrorTests.class)
                    .fetch();
        });

        String expectedMessage = "No matching EntityPathBase found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidFieldInSearchCriterion() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("invalidField", "eq", "Value"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Field not found in the given path";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUnsupportedOperatorInSearchCriterion() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("name", "unsupported", "Value"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Unsupported operator";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testSubQueryWithInvalidField() {
        SearchCriterion subCriterion = new SearchCriterion("nonExistentField", "eq", "Value");
        List<SearchCriterion> criteria = List.of(new SearchCriterion("book", "exists", List.of(subCriterion)));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Field not found in the given path";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidDateTypeCasting() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("establishedDate", "eq", "InvalidDate"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Failed to parse LocalDate from value";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testEmptyCriteriaList() {
        List<SearchCriterion> criteria = List.of();

        List<Library> result =
                criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        assertTrue(result.isEmpty());
    }

    @Test
    void testNullCriteria() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            criteriaQueryBuilder.buildQuery(null, Library.class).fetch();
        });

        String expectedMessage = "Cannot invoke";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidFieldTypeForCasting() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("memberships", "eq", "all"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Unsupported field type for dynamic casting";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidFieldTypeForComparison() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("isOpen", "gt", "true"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Comparison operators are not supported for the type of";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUnsupportedSubQueryOperator() {
        SearchCriterion subCriterion = new SearchCriterion("title", "eq", "Java Basics");
        List<SearchCriterion> criteria = List.of(new SearchCriterion("book", "in", List.of(subCriterion)));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Unsupported operator for sub queries";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidCollectionOperator() {
        List<SearchCriterion> criteria =
                List.of(new SearchCriterion("libraryId", "notACollectionOperator", "[test1,test2]"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Unsupported operator";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidCollectionValues() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("libraryId", "in", "[test1,test2]"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Invalid number format";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testEmptySubQueryCriterion() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("book", "exists", List.of()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Sub query criteria cannot be empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testNullSubQueryCriterion() {
        List<SearchCriterion> criteria = List.of(new SearchCriterion("book", "exists", List.of()));
        criteria.getFirst().setSubCriteria(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Sub query criteria cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testSubQueryWithInvalidCollectionField() {
        SearchCriterion subCriterion = new SearchCriterion("invalidField", "in", "[Value1,Value2]");
        List<SearchCriterion> criteria = List.of(new SearchCriterion("book", "exists", List.of(subCriterion)));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            criteriaQueryBuilder.buildQuery(criteria, Library.class).fetch();
        });

        String expectedMessage = "Field not found in the given path";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
