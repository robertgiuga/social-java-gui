package com.example.socialtpygui.domain;

import java.util.Objects;

public class TupleOne <E extends Comparable<E>> extends Tuple<E,E>{

    public TupleOne(E e, E e2) {
        super(e, e2);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if(!(obj instanceof TupleOne<?> tuple))
            return false;
        return (Objects.equals(getLeft(),tuple.getLeft())&&Objects.equals(getRight(),tuple.getRight()))||
                (Objects.equals(getLeft(),tuple.getRight())&&Objects.equals(getRight(),tuple.getLeft()));
    }

    @Override
    public int hashCode() {
        if(getLeft() == null||getLeft().compareTo(getRight())<0)
            return super.hashCode();
        return Objects.hash(getRight(),getLeft());
    }
}
