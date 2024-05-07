package dev.pablolec.querybuilder;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;


@RequiredArgsConstructor
public class CriteriaQueryBuilder {

    private final EntityManager entityManager;
    private final EntityPathResolver entityPathResolver;

    @SuppressWarnings("unchecked")
    public <T> JPAQuery<T> buildQuery(List<SearchCriterion> criteria, Class<T> targetClass) {
        EntityPathBase<T> rootEntityPath = (EntityPathBase<T>) entityPathResolver.getEntityPathBase(targetClass);
        JPAQuery<T> query = (JPAQuery<T>) new JPAQuery<>(entityManager).from(rootEntityPath);

        return (JPAQuery<T>) buildQuery(criteria, rootEntityPath, query);
    }

    public JPAQuery<?> buildQuery(List<SearchCriterion> criteria, String entityPathString) {
        EntityPathBase<?> rootEntityPath = entityPathResolver.getEntityPathBase(entityPathString);
        JPAQuery<?> query = new JPAQuery<>(entityManager).from(rootEntityPath);

        return buildQuery(criteria, rootEntityPath, query);
    }

    private JPAQuery<?> buildQuery(List<SearchCriterion> criteria, EntityPathBase<?> rootEntityPath, JPAQuery<?> query) {
        criteria.stream()
                .map(criterion -> buildExpressionRecursive(criterion, rootEntityPath))
                .forEach(query::where);

        return query;
    }

    private BooleanExpression buildExpressionRecursive(SearchCriterion criterion, EntityPathBase<?> currentEntityPath) {
        if (criterion.isSubQuery()) {
            validateSubQueryCriteria(criterion.getSubCriteria());

            EntityPathBase<?> childEntityPath = entityPathResolver.getEntityPathBase(criterion.getField());
            JPAQuery<?> subQuery = buildSubQuery(currentEntityPath, childEntityPath);
            buildQuery(criterion.getSubCriteria(), childEntityPath, subQuery);

            return ExpressionBuilder.buildSubQueryExpression(criterion, subQuery);
        } else {
            PathBuilder<?> pathBuilder = new PathBuilder<>(currentEntityPath.getType(), currentEntityPath.getMetadata().getName());
            return ExpressionBuilder.buildExpression(pathBuilder, criterion);
        }
    }

    private void validateSubQueryCriteria(List<SearchCriterion> subCriteria) {
        if (subCriteria == null) {
            throw new IllegalArgumentException("Sub query criteria cannot be null");
        } else if (subCriteria.isEmpty()) {
            throw new IllegalArgumentException("Sub query criteria cannot be empty");
        }
    }

    private JPAQuery<?> buildSubQuery(EntityPathBase<?> currentEntityPath, EntityPathBase<?> childEntityPath) {
        JPAQuery<?> subQuery = new JPAQuery<>();
        subQuery.from(childEntityPath);
        BooleanExpression joinCondition = getJoinCondition(currentEntityPath, childEntityPath);
        subQuery.where(joinCondition);

        return subQuery;
    }

    private BooleanExpression getJoinCondition(EntityPathBase<?> parentEntityPath, EntityPathBase<?> childEntityPath) {
        try {
            Class<?> parentEntityClass = parentEntityPath.getType();
            for (Field field : parentEntityClass.getDeclaredFields()) {
                Class<?> fieldType = field.getType();

                if (fieldType.equals(childEntityPath.getType())) {
                    return getDirectJoinCondition(parentEntityClass, parentEntityPath, childEntityPath, field);
                }

                if (Iterable.class.isAssignableFrom(fieldType)) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Class<?> collectionType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                    if (collectionType.equals(childEntityPath.getType())) {
                        return getCollectionJoinCondition(parentEntityClass, parentEntityPath, childEntityPath, field);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error finding join condition between entities", e);
        }

        throw new IllegalStateException("No valid join condition found between entities "
                + parentEntityPath.getType().getSimpleName()
                + " and "
                + childEntityPath.getType().getSimpleName());
    }

    private BooleanExpression getDirectJoinCondition(Class<?> parentEntityClass, EntityPathBase<?> parentEntityPath, EntityPathBase<?> childEntityPath, Field field) {
        PathBuilder<?> parentEntityBuilder = new PathBuilder<>(parentEntityClass, parentEntityPath.getMetadata().getName());
        return parentEntityBuilder.get(field.getName()).get("id").eq(new PathBuilder<>(childEntityPath.getType(), childEntityPath.getMetadata().getName()).get("id"));
    }

    private BooleanExpression getCollectionJoinCondition(Class<?> parentEntityClass, EntityPathBase<?> parentEntityPath, EntityPathBase<?> childEntityPath, Field field) {
        PathBuilder<?> parentEntityBuilder = new PathBuilder<>(parentEntityClass, parentEntityPath.getMetadata().getName());
        return parentEntityBuilder.getCollection(field.getName(), parentEntityClass).any().get("id").eq(new PathBuilder<>(childEntityPath.getType(), childEntityPath.getMetadata().getName()).get("id"));
    }
}

