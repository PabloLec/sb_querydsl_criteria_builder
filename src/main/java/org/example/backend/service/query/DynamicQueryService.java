package org.example.backend.service.query;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.example.backend.service.query.QueryDslUtils.getEntityPathBase;


@Service
@Slf4j
public class DynamicQueryService {

    private final EntityManager entityManager;

    public DynamicQueryService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public JPAQuery<?> buildDynamicQuery(List<SearchCriterion> criteria, String entityPathString) {
        EntityPathBase<?> entityPathBase = getEntityPathBase(entityPathString);
        JPAQuery<?> query = new JPAQuery<>(entityManager).from(entityPathBase);

        for (SearchCriterion criterion : criteria) {
            BooleanExpression expression = criterion.isSubQuery() ?
                    handleSubQuery(criterion, entityPathBase, entityPathString) :
                    buildExpression(new PathBuilder<>(entityPathBase.getType(), entityPathBase.getMetadata().getName()), criterion);

            query.where(expression);
        }

        return query;
    }

    private BooleanExpression handleSubQuery(SearchCriterion criterion, EntityPathBase<?> rootEntityPath, String rootEntityPathString) {
        String relatedEntityName = criterion.getField();
        EntityPathBase<?> relatedEntityPath = getEntityPathBase(relatedEntityName);
        PathBuilder<?> relatedEntityBuilder = new PathBuilder<>(relatedEntityPath.getType(), relatedEntityPath.getMetadata().getName());
        PathBuilder<?> rootEntityBuilder = new PathBuilder<>(rootEntityPath.getType(), rootEntityPath.getMetadata().getName());
        BooleanExpression fieldCondition = buildExpression(relatedEntityBuilder, criterion.getSubCriterion());
        JPAQuery<?> subQuery = new JPAQuery<>();
        subQuery.from(relatedEntityPath);
        BooleanExpression joinCondition = relatedEntityBuilder.get(rootEntityPathString).get("id").eq(rootEntityBuilder.get("id"));
        subQuery.where(fieldCondition, joinCondition);
        return buildSubQueryExpression(criterion, subQuery);
    }

    private BooleanExpression buildExpression(PathBuilder<?> entityPath, SearchCriterion criterion) {
        boolean isCollection = criterion.getOp().equals("in") || criterion.getOp().equals("notIn");
        Object castedValue = castStringToFieldType(entityPath.getType(), criterion.getField(), criterion.getValue(), isCollection);

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

    private Object castStringToFieldType(Class<?> entityClass, String fieldName, String value, boolean isCollection) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            Class<?> fieldType = field.getType();
            if (!isCollection) {
                return castSingleValue(fieldType, value);
            } else {
                Type genericFieldType = field.getGenericType();
                return castCollectionValue((Class<?>) genericFieldType, value);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + fieldName, e);
        }
    }

    private Object castSingleValue(Class<?> fieldType, String value) {
        if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
            return Integer.parseInt(value);
        } else if (Long.class.equals(fieldType) || long.class.equals(fieldType)) {
            return Long.parseLong(value);
        } else if (Double.class.equals(fieldType) || double.class.equals(fieldType)) {
            return Double.parseDouble(value);
        } else if (LocalDate.class.equals(fieldType)) {
            return LocalDate.parse(value);
        } else if (LocalDateTime.class.equals(fieldType)) {
            return LocalDateTime.parse(value);
        } else if (String.class.equals(fieldType)) {
            return value;
        } else {
            throw new IllegalArgumentException("Unsupported field type for dynamic casting: " + fieldType.getSimpleName());
        }
    }

    private List<?> castCollectionValue(Class<?> elementType, String value) {
        String[] elements = value.replace("[", "").replace("]", "").split(",");
        return Arrays.stream(elements).map(element -> castSingleValue(elementType, element.trim())).toList();
    }

    private BooleanExpression handleBasicComparisons(PathBuilder<?> entityPath, String fieldName, Object value, String operation) {
        return switch (operation) {
            case "eq" -> entityPath.get(fieldName).eq(value);
            case "ne" -> entityPath.get(fieldName).ne(value);
            default -> throw new IllegalStateException("Unexpected operator for basic comparisons: " + operation);
        };
    }

    private BooleanExpression handleCollectionExpression(PathBuilder<?> entityPath, String fieldName, List<?> value, String operation) {
        return switch (operation) {
            case "in" -> entityPath.get(fieldName).in(value);
            case "notIn" -> entityPath.get(fieldName).notIn(value);
            default -> throw new IllegalStateException("Unexpected collection operator: " + operation);
        };
    }


    private BooleanExpression handleComparisonOperators(PathBuilder<?> entityPath, String fieldName, Object value, String operation) {
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

    private BooleanExpression compareUsingIntegerPath(NumberPath<Integer> path, Integer value, String operation) {
        return switch (operation) {
            case "gt" -> path.gt(value);
            case "lt" -> path.lt(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }

    private BooleanExpression compareUsingLongPath(NumberPath<Long> path, Long value, String operation) {
        return switch (operation) {
            case "gt" -> path.gt(value);
            case "lt" -> path.lt(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }

    private BooleanExpression compareUsingDatePath(DatePath<LocalDate> path, LocalDate value, String operation) {
        return switch (operation) {
            case "gt" -> path.after(value);
            case "lt" -> path.before(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }

    private BooleanExpression compareUsingDateTimePath(DateTimePath<LocalDateTime> path, LocalDateTime value, String operation) {
        return switch (operation) {
            case "gt" -> path.after(value);
            case "lt" -> path.before(value);
            case "gte" -> path.goe(value);
            case "lte" -> path.loe(value);
            default -> throw new IllegalStateException("Unexpected comparison operator: " + operation);
        };
    }

    private BooleanExpression buildSubQueryExpression(SearchCriterion criterion, JPAQuery<?> subQuery) {
        return switch (criterion.getOp()) {
            case "exists" -> Expressions.booleanTemplate("EXISTS ({0})", subQuery);
            case "notExists" -> Expressions.booleanTemplate("NOT EXISTS ({0})", subQuery);
            default -> throw new IllegalArgumentException("Unsupported operator for sub queries: " + criterion.getOp());
        };
    }
}


