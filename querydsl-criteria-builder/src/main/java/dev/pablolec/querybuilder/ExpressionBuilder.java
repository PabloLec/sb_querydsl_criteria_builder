package dev.pablolec.querybuilder;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ExpressionBuilder {
    public static BooleanExpression buildExpression(PathBuilder<?> entityPath, SearchCriterion criterion) {
        boolean isCollection = criterion.getOp().equals("in") || criterion.getOp().equals("notIn");
        Object castedValue = DynamicFieldCaster.castValue(entityPath.getType(), criterion.getField(), criterion.getValue(), isCollection);

        return switch (criterion.getOp()) {
            case "eq", "ne" -> handleBasicComparisons(entityPath, criterion.getField(), castedValue, criterion.getOp());
            case "like" -> {
                if (!(castedValue instanceof String)) {
                    throw new IllegalArgumentException("LIKE operation is only valid for String types.");
                }
                yield entityPath.getString(criterion.getField()).like((String) castedValue);
            }
            case "gt", "lt", "gte", "lte" ->
                    handleComparisonOperators(entityPath, criterion.getField(), castedValue, criterion.getOp());
            case "in", "notIn" ->
                    handleCollectionExpression(entityPath, criterion.getField(), (List<?>) castedValue, criterion.getOp());
            default -> throw new IllegalArgumentException("Unsupported operator: " + criterion.getOp());
        };
    }

    public static BooleanExpression buildSubQueryExpression(SearchCriterion criterion, JPAQuery<?> subQuery) {
        return switch (criterion.getOp()) {
            case "exists" -> Expressions.booleanTemplate("EXISTS ({0})", subQuery);
            case "notExists" -> Expressions.booleanTemplate("NOT EXISTS ({0})", subQuery);
            default -> throw new IllegalArgumentException("Unsupported operator for sub queries: " + criterion.getOp());
        };
    }

    private static BooleanExpression handleBasicComparisons(PathBuilder<?> entityPath, String fieldName, Object value, String operation) {
        return switch (operation) {
            case "eq" -> entityPath.get(fieldName).eq(value);
            case "ne" -> entityPath.get(fieldName).ne(value);
            default -> throw new IllegalStateException("Unexpected operator for basic comparisons: " + operation);
        };
    }

    private static BooleanExpression handleCollectionExpression(PathBuilder<?> entityPath, String fieldName, List<?> value, String operation) {
        return switch (operation) {
            case "in" -> entityPath.get(fieldName).in(value);
            case "notIn" -> entityPath.get(fieldName).notIn(value);
            default -> throw new IllegalStateException("Unexpected collection operator: " + operation);
        };
    }


    private static BooleanExpression handleComparisonOperators(PathBuilder<?> entityPath, String fieldName, Object value, String operation) {
        if (value instanceof Integer integerValue) {
            return compareUsingIntegerPath(entityPath.getNumber(fieldName, Integer.class), integerValue, operation);
        } else if (value instanceof Long longValue) {
            return compareUsingLongPath(entityPath.getNumber(fieldName, Long.class), longValue, operation);
        } else if (value instanceof LocalDate localDateValue) {
            return compareUsingDatePath(entityPath.getDate(fieldName, LocalDate.class), localDateValue, operation);
        } else if (value instanceof LocalDateTime localDateTimeValue) {
            return compareUsingDateTimePath(entityPath.getDateTime(fieldName, LocalDateTime.class), localDateTimeValue, operation);
        }
        throw new IllegalArgumentException("Comparison operations are not supported for the type of " + fieldName + ": " + value.getClass().getSimpleName());
    }

    private static BooleanExpression compareUsingIntegerPath(NumberPath<Integer> path, Integer value, String operation) {
        return switch (operation) {
            case "gt" -> path.gt(value);
            case "lt" -> path.lt(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }

    private static BooleanExpression compareUsingLongPath(NumberPath<Long> path, Long value, String operation) {
        return switch (operation) {
            case "gt" -> path.gt(value);
            case "lt" -> path.lt(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }

    private static BooleanExpression compareUsingDatePath(DatePath<LocalDate> path, LocalDate value, String operation) {
        return switch (operation) {
            case "gt" -> path.after(value);
            case "lt" -> path.before(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }

    private static BooleanExpression compareUsingDateTimePath(DateTimePath<LocalDateTime> path, LocalDateTime value, String operation) {
        return switch (operation) {
            case "gt" -> path.after(value);
            case "lt" -> path.before(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }
}
