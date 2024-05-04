package org.example.backend.service.query.querydsl;

import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class QueryDslClassMapper {
    private final Map<String, EntityPathBase<?>> entityMappings;

    @Autowired
    public QueryDslClassMapper(Map<String, EntityPathBase<?>> entityMappings) {
        if (entityMappings == null || entityMappings.isEmpty()) {
            throw new IllegalArgumentException("Entity mappings cannot be null or empty");
        }
        this.entityMappings = Collections.unmodifiableMap(entityMappings);
    }

    public EntityPathBase<?> getEntityPathBase(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key is null or empty");
        }
        EntityPathBase<?> entityPath = entityMappings.get(key);
        if (entityPath == null) {
            throw new IllegalStateException("No matching EntityPathBase found for '" + key + "'");
        }
        return entityPath;
    }
}
