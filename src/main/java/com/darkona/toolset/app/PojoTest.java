package com.darkona.toolset.app;

import com.darkona.toolset.logging.LogStrings;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.beans.Expression;
import java.beans.Statement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PojoTest {

    private static final List<Class<?>> classesUnderTest = new ArrayList<>();


    public static void setPojos(List<Class<?>> classes) {

        classesUnderTest.addAll(classes);
    }

    public static void testPojos() throws Exception{

        for (Class<?> clazz : classesUnderTest) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for(var field : FieldUtils.getAllFieldsList(clazz)) {

                field.setAccessible(true);
                field.set(instance, getValueForField(field));

                var mockValue = getValueForField(field);
                var setter = getSetterName(field);
                var getter = getGetterName(field);


                new Statement(instance, setter, new Object[]{mockValue}).execute();
                Expression get = new Expression(instance, getter, new Object[]{});

                get.execute();

                Object value = get.getValue();


                assertEquals(mockValue, value, "Getter returns what the setter set for field " + field.getName());
            }
        }

    }


    private static Object getValueForField(Field field)
    throws Exception {
        Class<?> type = field.getType();

        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType paramType) {
            Type rawType = paramType.getRawType();

            if (rawType instanceof Map<?, ?>) {
                Type[] typeArgs = paramType.getActualTypeArguments();
                return createMockMap(typeArgs);
            }

            if (rawType instanceof List<?>) {
                Type[] typeArgs = paramType.getActualTypeArguments();
                return createMockList(typeArgs);
            }

            if (rawType == Array.class) {
                Type[] typeArgs = paramType.getActualTypeArguments();
                return createMockArray(typeArgs[0]);
            }

        }

        return getMockValue(type);

    }

    private static Object getMockValue(Class<?> type) {

        if (type.isAssignableFrom(String.class)) {
            return "Hello World";
        }

        if (type.isAssignableFrom(Integer.class)) {
            return 32767;
        }

        if (type.isAssignableFrom(Long.class)) {
            return 42L;
        }

        if (type.isAssignableFrom(Double.class)) {
            return 42.0;
        }

        if (type.isAssignableFrom(Float.class)) {
            return 42.0f;
        }

        if (type.isAssignableFrom(Boolean.class)) {
            return true;
        }

        if (type.isAssignableFrom(Character.class)) {
            return 'c';
        }

        if (type.isAssignableFrom(Byte.class)) {
            return (byte) 42;
        }

        if (type.isAssignableFrom(Short.class)) {
            return (short) 42;
        }

        if (type.isEnum()) {
            return type.getEnumConstants()[0];
        }

        if (type.isAssignableFrom(LocalDateTime.class)) {
            return LocalDateTime.now();
        }

        if (type.isAssignableFrom(Instant.class)) {
            return Instant.now();
        }

        if (type.isAssignableFrom(Collection.class)) {
            return new ArrayList<>();
        }

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception ignore) {}

        return null;
    }

    private static String getSetterName(Field field) {
        return "set" + LogStrings.capitalize(field.getName());
    }

    private static String getGetterName(Field field) {
        return "get" + LogStrings.capitalize(field.getName());
    }

    private static Map<?, ?> createMockMap(Type[] typeArgs) {
        var key = getMockValue((Class<?>) typeArgs[0]);
        var value = getMockValue((Class<?>) typeArgs[1]);
        return Map.of(key != null ? key : new Object(), value != null ? value : new Object());
    }

    private static List<?> createMockList(Type[] typeArgs) {
        var value = getMockValue((Class<?>) typeArgs[0]);
        return List.of(value != null ? value : new Object());
    }

    private static Set<?> createMockSet(Type[] typeArgs) {
        var value = getMockValue((Class<?>) typeArgs[0]);
        return Set.of(value != null ? value : new Object());
    }

    private static Object[] createMockArray(Type type) {
        return new Object[]{getMockValue((Class<?>) type)};
    }



}
