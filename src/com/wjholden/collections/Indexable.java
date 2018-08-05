package com.wjholden.collections;

public interface Indexable<T> {
    T get(int i);
    boolean set(int i, T t);
    T delete(int i);
    int indexOf(T t);
    //Indexable<T> range(int first, int last);
}
