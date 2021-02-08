package com.chapter05.item31.max;

import java.util.Objects;

public class Parent implements Comparable<Parent> {

    private final int id;

    public Parent(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Parent o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parent parent = (Parent) o;
        return id == parent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
