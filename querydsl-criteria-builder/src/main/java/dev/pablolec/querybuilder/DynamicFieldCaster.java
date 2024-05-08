package dev.pablolec.querybuilder;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
class DynamicFieldCaster {
    public static Object castValue(Class<?> entityClass, String fieldName, String value, boolean isCollection) {
        try {
            Class<?> currentType = entityClass;
            Field field = null;

            String[] fieldParts = fieldName.split("\\.");
            for (String part : fieldParts) {
                field = getField(currentType, part);
                currentType = field.getType();
            }

            if (field == null) {
                throw new IllegalArgumentException("Field not found in the given path: " + fieldName);
            }

            if (isCollection) {
                Class<?> fieldType = field.getType();
                return castCollection(fieldType, value);
            } else {
                return castSingleValue(currentType, value);
            }
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found in the given path: " + fieldName, e);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + value + " on field: " + fieldName, e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + value + " on field: " + fieldName, e);
        }
    }

    private static Field getField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field not found: " + fieldName);
    }

    private static Object castSingleValue(Class<?> fieldType, String value) {
        String typeName = fieldType.getCanonicalName();
        return switch (typeName) {
            case "java.lang.Integer", "int" -> Integer.parseInt(value);
            case "java.lang.Long", "long" -> Long.parseLong(value);
            case "java.time.LocalDate" -> LocalDate.parse(value);
            case "java.time.LocalDateTime" -> LocalDateTime.parse(value);
            case "java.lang.String" -> value;
            default -> throw new IllegalArgumentException(
                    "Unsupported field type for dynamic casting: " + fieldType.getSimpleName());
        };
    }

    private static List<?> castCollection(Class<?> fieldType, String value) {
        String[] elements = value.replace("[", "").replace("]", "").split(",");

        return Arrays.stream(elements)
                .map(element -> castSingleValue(fieldType, element.trim()))
                .toList();
    }
}
