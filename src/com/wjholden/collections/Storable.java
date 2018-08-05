package com.wjholden.collections;

public interface Storable<T> {
    boolean add(T t);
    int size();
    T remove(T t);
    boolean contains(T t);
    void clear();
    //Storable<T> merge(Storable<T> s);
}
