package com.chapter05.item31.swap;

import java.util.List;

public class SwapWildUtil {

    public static void swap(List<?> list, int i, int j) {
        // list.set(i, list.set(j, list.get(i))); // コンパイルエラー
        swapHelper(list, i, j);
    }

    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(j)));
    }

    private SwapWildUtil() {}

}
