package com.ndt.be_stepupsneaker.util;

import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EntityComparator {

    private static final Set<Class<?>> BASE_TYPES = new HashSet<>(Arrays.asList(
            String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class, OrderStatus.class));

    public static <T> boolean isBaseType(Class<T> clazz) {
        return BASE_TYPES.contains(clazz);
    }

}
