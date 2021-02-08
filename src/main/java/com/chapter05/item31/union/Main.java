package com.chapter05.item31.union;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<Integer> integerSet = Set.of(1, 2, 3);

        // com.chapter05.item28.Chooser<Number> chooser28 = new com.chapter05.item28.Chooser<>(integerSet); // コンパイルエラー
        com.chapter05.item31.chooser.Chooser<Number> chooser31 = new com.chapter05.item31.chooser.Chooser<>(integerSet);
    }

}
