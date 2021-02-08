package com.chapter05.item31.stack;

import com.chapter05.item29.Stack;

import java.util.Collection;

public class StackWildcard<E> extends Stack<E> {

    public void pushAll(Iterable<? extends E> src) {
        for (E e : src) {
            push(e);
        }
    }

    public void popAll(Collection<? super E> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }

}
