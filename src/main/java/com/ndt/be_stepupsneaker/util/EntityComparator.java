package com.ndt.be_stepupsneaker.util;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EntityComparator {

    public static void difference(Object s1, Object s2, List<String> changedProperties, String parent, Set<Object> visitedObjects) throws IllegalAccessException {
        if (visitedObjects.contains(s1) || visitedObjects.contains(s2)) {
            // Avoid infinite recursion for circular references
            return;
        }

        visitedObjects.add(s1);
        visitedObjects.add(s2);

        for (Field field : s1.getClass().getDeclaredFields()) {
            if (parent == null) {
                parent = s1.getClass().getSimpleName();
            }

            // Skip private static final fields
            int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                continue;
            }

            field.setAccessible(true);
            Object value1 = field.get(s1);
            Object value2 = field.get(s2);

            if (value1 == null && value2 == null) {
                continue;
            }
            if (value1 == null || value2 == null) {
                changedProperties.add(parent + "." + field.getName());
            } else {
                if (isBaseType(value1.getClass())) {
                    if (!Objects.equals(value1, value2)) {
                        changedProperties.add(parent + "." + field.getName());
                    }
                } else {
                    difference(value1, value2, changedProperties, parent + "." + field.getName(), visitedObjects);
                }
            }
        }
    }



    private static final Set<Class<?>> BASE_TYPES = new HashSet<>(Arrays.asList(
            String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class));

    public static <T> boolean isBaseType(Class<T> clazz) {
        return BASE_TYPES.contains(clazz);
    }


}
