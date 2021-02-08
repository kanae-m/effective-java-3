package com.chapter05.item31.swap;

import java.util.List;

public class SwapParamUtil {

    public static <E> void swap(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(j)));
    }

    private SwapParamUtil() {}

}
