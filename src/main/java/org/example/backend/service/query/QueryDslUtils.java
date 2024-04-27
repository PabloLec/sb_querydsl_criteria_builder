package org.example.backend.service.query;

import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
@UtilityClass
public class QueryDslUtils {

    private static final String BASE_PACKAGE = "org.example.backend.db.model";

    public static EntityPathBase<?> getEntityPathBase(String entityName) {
        if (entityName == null || entityName.isEmpty()) {
            log.error("Entity name is null or empty");
            throw new IllegalArgumentException("Entity name is null or empty");
        }

        String className = "Q" + capitalize(entityName);
        try {
            Class<?> qClass = Class.forName(BASE_PACKAGE + "." + className);
            Field instanceField = qClass.getField(entityName.toLowerCase());

            return (EntityPathBase<?>) instanceField.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            log.error("Error while retrieving EntityPathBase for '{}': {}", entityName, e.getMessage());
            throw new IllegalStateException("Error while retrieving EntityPathBase for '" + entityName + "'", e);
        }
    }

    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}