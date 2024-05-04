package org.example.backend.service.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.service.query.querydsl.QueryDslClassMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.backend.service.query.ExpressionBuilder.buildExpression;
import static org.example.backend.service.query.ExpressionBuilder.buildSubQueryExpression;

@Service
@Slf4j
@RequiredArgsConstructor
public class DynamicQueryService {

    private final EntityManager entityManager;
    private final QueryDslClassMapper queryDslClassMapper;

    public JPAQuery<?> buildDynamicQuery(List<SearchCriterion> criteria, String entityPathString) {
        EntityPathBase<?> entityPathBase = queryDslClassMapper.getEntityPathBase(entityPathString);
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
        EntityPathBase<?> relatedEntityPath = queryDslClassMapper.getEntityPathBase(relatedEntityName);
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

