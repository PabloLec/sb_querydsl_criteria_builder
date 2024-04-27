package org.example.backend.service.query;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@UtilityClass
class TypeHelper {

    public static Object castValue(Class<?> entityClass, String fieldName, String value, boolean isCollection) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            Class<?> fieldType = field.getType();
            if (isCollection) {
                return castCollection(fieldType, value);
            } else {
                return castSingleValue(fieldType, value);
            }
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found: " + fieldName, e);
        }
    }

    private static Object castSingleValue(Class<?> fieldType, String value) {
        String typeName = fieldType.getCanonicalName();
        return switch (typeName) {
            case "java.lang.Integer", "int" -> Integer.parseInt(value);
            case "java.lang.Long", "long" -> Long.parseLong(value);
            case "java.time.LocalDate" -> LocalDate.parse(value);
            case "java.time.LocalDateTime" -> LocalDateTime.parse(value);
            case "java.lang.String" -> value;
            default -> throw new IllegalArgumentException("Unsupported field type for dynamic casting: " + fieldType.getSimpleName());
        };
    }

    private static List<?> castCollection(Class<?> fieldType, String value) {
        String[] elements = value.replace("[", "").replace("]", "").split(",");
        return Arrays.stream(elements).map(element -> castSingleValue(fieldType, element.trim())).toList();
    }
}