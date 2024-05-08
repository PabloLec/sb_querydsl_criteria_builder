package dev.pablolec.querybuilder;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
class DynamicFieldCaster {
    public static Object castValue(Class<?> entityClass, String fieldName, String value, boolean isCollection) {
        try {
            Class<?> fieldType = resolveFieldType(entityClass, fieldName);
            return isCollection ? castCollection(fieldType, value) : castSingleValue(fieldType, value);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found in the given path: " + fieldName, e);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + value + " on field: " + fieldName, e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + value + " on field: " + fieldName, e);
        }
    }

    private static Class<?> resolveFieldType(Class<?> entityClass, String fieldName) throws NoSuchFieldException {
        Class<?> currentType = entityClass;
        for (String part : Arrays.stream(fieldName.split("\\.")).toList()) {
            Field field = getField(currentType, part);
            currentType = field.getType();
        }
        return currentType;
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
        if (String.class.equals(fieldType)) {
            return value;
        }
        return castWithFallback(fieldType, value);
    }

    private static Object castWithFallback(Class<?> fieldType, String value) {
        try {
            return parseWithReflection(fieldType, value);
        } catch (NoSuchMethodException e) {
            return castWithPropertyEditor(fieldType, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Failed to parse " + fieldType.getSimpleName() + " from value: " + value, e);
        }
    }

    private static Object parseWithReflection(Class<?> fieldType, String value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method parseMethod = fieldType.getMethod("parse", CharSequence.class);
        return parseMethod.invoke(null, value);
    }

    private static Object castWithPropertyEditor(Class<?> fieldType, String value) {
        PropertyEditor editor = PropertyEditorManager.findEditor(fieldType);
        if (editor == null) {
            throw new IllegalArgumentException(
                    "Unsupported field type for dynamic casting: " + fieldType.getSimpleName());
        }
        editor.setAsText(value);
        return getValueFromPropertyEditor(editor, fieldType, value);
    }

    private static Object getValueFromPropertyEditor(PropertyEditor editor, Class<?> fieldType, String value) {
        try {
            return editor.getValue();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Failed to convert String to " + fieldType.getSimpleName() + ": " + value, e);
        }
    }

    private static List<?> castCollection(Class<?> fieldType, String value) {
        String[] elements = value.replace("[", "").replace("]", "").split(",");

        return Arrays.stream(elements)
                .map(element -> castSingleValue(fieldType, element.trim()))
                .toList();
    }
}
