package com.wjholden.collections;

public interface Sortable<T> {
    boolean isSorted();
    //Sortable<T> reverse();
    //T majority();
    T min();
    T max();
    T successor(T t);
    T predecessor(T t);
    Sortable<T> subset(T first, T last);
}
