package dev.pablolec.querybuilder;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import dev.pablolec.querybuilder.model.Operator;
import dev.pablolec.querybuilder.model.SearchCriterion;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExpressionBuilder {
    public static BooleanExpression buildExpression(SearchCriterion criterion, EntityPathBase<?> rootEntityPath) {
        PathBuilder<?> pathBuilder = new PathBuilder<>(
                rootEntityPath.getType(), rootEntityPath.getMetadata().getName());
        Operator operator = Operator.fromString(criterion.getOp());
        Object castedValue = DynamicFieldCaster.castValue(
                pathBuilder.getType(), criterion.getField(), criterion.getValue(), operator.isCollectionOperator());

        return switch (operator) {
            case EQ, NE -> handleEqualityOperators(pathBuilder, criterion.getField(), castedValue, operator);
            case LIKE, NOT_LIKE -> handleLikeOperators(pathBuilder, criterion.getField(), castedValue, operator);
            case IN, NOT_IN -> handleCollectionOperators(
                    pathBuilder, criterion.getField(), (List<?>) castedValue, operator);
            case GT, LT, GTE, LTE -> handleComparisonOperators(
                    pathBuilder, criterion.getField(), castedValue, operator);
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

    public static BooleanExpression buildSubQueryExpression(SearchCriterion criterion, JPAQuery<?> subQuery) {
        Operator operator = Operator.fromString(criterion.getOp());
        return switch (operator) {
            case EXISTS -> Expressions.booleanTemplate("EXISTS ({0})", subQuery);
            case NOT_EXISTS -> Expressions.booleanTemplate("NOT EXISTS ({0})", subQuery);
            default -> throw new IllegalArgumentException("Unsupported operator for sub queries: " + operator);
        };
    }

    private static BooleanExpression handleEqualityOperators(
            PathBuilder<?> entityPath, String fieldName, Object value, Operator operator) {
        return switch (operator) {
            case EQ -> entityPath.get(fieldName).eq(value);
            case NE -> entityPath.get(fieldName).ne(value);
            default -> throw new IllegalStateException("Unexpected equality operator: " + operator);
        };
    }

    private static BooleanExpression handleLikeOperators(
            PathBuilder<?> entityPath, String fieldName, Object value, Operator operator) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException(operator + " operator only supports String values. Field: " + fieldName
                    + ", Value type: " + value.getClass().getSimpleName());
        }

        return switch (operator) {
            case LIKE -> entityPath.getString(fieldName).like((String) value);
            case NOT_LIKE -> entityPath.getString(fieldName).notLike((String) value);
            default -> throw new IllegalStateException("Unexpected LIKE operator: " + operator);
        };
    }

    private static BooleanExpression handleCollectionOperators(
            PathBuilder<?> entityPath, String fieldName, List<?> value, Operator operator) {
        return switch (operator) {
            case IN -> entityPath.get(fieldName).in(value);
            case NOT_IN -> entityPath.get(fieldName).notIn(value);
            default -> throw new IllegalStateException("Unexpected collection operator: " + operator);
        };
    }

    private static BooleanExpression handleComparisonOperators(
            PathBuilder<?> entityPath, String fieldName, Object value, Operator operator) {
        if (!(value instanceof Comparable)) {
            throw new IllegalArgumentException("Comparison operators are only supported for Comparable types. Field: "
                    + fieldName + ", Value type: " + value.getClass().getSimpleName());
        }

        return switch (value) {
            case Number numberValue -> compareUsingGenericNumberPath(entityPath, fieldName, numberValue, operator);
            case TemporalAccessor temporalValue -> compareUsingGenericTemporalPath(
                    entityPath, fieldName, temporalValue, operator);
            default -> throw new IllegalArgumentException("Comparison operators are not supported for the type of "
                    + fieldName + ": " + value.getClass().getSimpleName());
        };
    }

    private static <N extends Number & Comparable<N>> BooleanExpression compareUsingGenericNumberPath(
            PathBuilder<?> entityPath, String fieldName, Number numberValue, Operator operator) {
        @SuppressWarnings("unchecked")
        Class<N> type = (Class<N>) numberValue.getClass();
        NumberPath<N> path = entityPath.getNumber(fieldName, type);
        N castedValue = type.cast(numberValue);

        return switch (operator) {
            case GT -> path.gt(castedValue);
            case LT -> path.lt(castedValue);
            case GTE -> path.goe(castedValue);
            case LTE -> path.loe(castedValue);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operator);
        };
    }

    private static <T extends Comparable<T>> BooleanExpression compareUsingGenericTemporalPath(
            PathBuilder<?> entityPath, String fieldName, TemporalAccessor temporalValue, Operator operator) {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) temporalValue.getClass();
        TemporalExpression<T> path = entityPath.getTime(fieldName, type);
        T castedValue = type.cast(temporalValue);

        return switch (operator) {
            case GT -> path.after(castedValue);
            case LT -> path.before(castedValue);
            case GTE -> path.goe(castedValue);
            case LTE -> path.loe(castedValue);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operator);
        };
    }
}
