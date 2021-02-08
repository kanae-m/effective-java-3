package com.chapter05.item31.chooser;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<Integer> integerSet = Set.of(1, 3, 5);
        Set<Double> doubleSet = Set.of(2.0, 4.0, 6.0);

        // Set<Number> numbers1 = com.chapter05.item30.union.UnionUtil.union(integerSet, doubleSet); // コンパイルエラー
        Set<Number> numbers2 = com.chapter05.item31.union.UnionUtil.union(integerSet, doubleSet);
    }

}
