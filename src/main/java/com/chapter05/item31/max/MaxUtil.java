package com.chapter05.item31.max;

import java.util.Collection;
import java.util.Objects;

public class MaxUtil {

    public static <E extends Comparable<? super E>> E max(Collection<? extends E> c) {
        if (c.isEmpty()) {
            throw new IllegalArgumentException("Empty collection");
        }
        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return result;
    }

    private MaxUtil() {}

}
