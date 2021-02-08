package com.chapter05.item31.stack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Iterable<Integer> integers = Set.of(1, 2, 3);
        Collection<Object> objects = new ArrayList<>();

        StackNotWildcard<Number> numberStackNotWildcard = new StackNotWildcard<>();
        // numberStackNotWildcard.pushAll(integers); // コンパイルエラー
        // numberStackNotWildcard.popAll(objects); // コンパイルエラー

        StackWildcard<Number> numberStackWildcard = new StackWildcard<>();
        numberStackWildcard.pushAll(integers);
        numberStackWildcard.popAll(objects);
    }

}
