package org.example.backend.service.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.backend.service.query.ExpressionBuilder.buildExpression;
import static org.example.backend.service.query.ExpressionBuilder.buildSubQueryExpression;
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
}

