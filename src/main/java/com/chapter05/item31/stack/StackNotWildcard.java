package com.chapter05.item31.stack;

import com.chapter05.item29.Stack;

import java.util.Collection;

public class StackNotWildcard<E> extends Stack<E> {

    public void pushAll(Iterable<E> src) {
        for (E e : src) {
            push(e);
        }
    }

    public void popAll(Collection<E> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }

}
