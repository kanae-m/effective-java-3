package com.chapter05.item31.max;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Child child1 = new Child(1);
        Child child2 = new Child(2);

        List<Child> childList = List.of(child1, child2);

        // Child maxChild = com.chapter05.item30.max.MaxUtil.max(childList); // コンパイルエラー
        Child maxChild = com.chapter05.item31.max.MaxUtil.max(childList);
    }

}
