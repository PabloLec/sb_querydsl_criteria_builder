package org.example.backend.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.backend.service.QueryDslUtils.getEntityPathBase;


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
        return switch (criterion.getOp()) {
            case "eq" -> entityPath.get(criterion.getField()).eq(criterion.getValue());
            case "ne" -> entityPath.get(criterion.getField()).ne(criterion.getValue());
            case "like" -> entityPath.getString(criterion.getField()).like(criterion.getValue().toString());
            default -> throw new IllegalArgumentException("Unsupported operator: " + criterion.getOp());
        };
    }

    private BooleanExpression buildSubQueryExpression(SearchCriterion criterion, JPAQuery<?> subQuery) {
        return switch (criterion.getOp()) {
            case "exists" -> Expressions.booleanTemplate("EXISTS ({0})", subQuery);
            case "notExists" -> Expressions.booleanTemplate("NOT EXISTS ({0})", subQuery);
            default -> throw new IllegalArgumentException("Unsupported operator for sub queries: " + criterion.getOp());
        };
    }

    @Data
    @AllArgsConstructor
    public static class SearchCriterion {
        private String field;
        private String op;
        private Object value;
        private boolean subQuery;
        private SearchCriterion subCriterion;

        public SearchCriterion(String field, String op, Object value) {
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
}

