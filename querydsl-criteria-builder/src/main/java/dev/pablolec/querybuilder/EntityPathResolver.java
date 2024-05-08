package dev.pablolec.querybuilder;

import com.querydsl.core.types.dsl.EntityPathBase;
import java.util.Collections;
import java.util.Map;

public class EntityPathResolver {
    private final Map<String, EntityPathBase<?>> entityMappings;

    public EntityPathResolver(Map<String, EntityPathBase<?>> entityMappings) {
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

    public EntityPathBase<?> getEntityPathBase(Class<?> targetClass) {
        if (targetClass == null) {
            throw new IllegalArgumentException("Target class is null");
        }

        for (EntityPathBase<?> entityPath : entityMappings.values()) {
            if (entityPath.getRoot().getType().equals(targetClass)) {
                return entityPath;
            }
        }

        throw new IllegalStateException("No matching EntityPathBase found for '" + targetClass.getSimpleName() + "'");
    }
}
