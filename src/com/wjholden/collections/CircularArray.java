package com.wjholden.collections;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CircularArray<T> implements Storable<T>, Indexable<T>, FrontEndable<T>, BackEndable<T> {

    private int count;
    private int head;
    private final T data[];
    private final int capacity;
    
    public CircularArray(int size) {
        count = head = 0;
        capacity = size;
        data = (T[]) new Object[capacity];
    }
    
    public static void main(String[] args) {
        CircularArray<String> a = new CircularArray<>(10);
        try (Scanner s = new Scanner(System.in)) {
            System.out.print("Command: " );
            while (s.hasNextLine()) {
                String command = s.next();
                int x = 0;
                if (s.hasNextInt()) x = s.nextInt();
                String value = s.nextLine();
                switch (command) {
                    case "add": a.add(value); break;
                    case "remove": a.remove(value); break;
                    case "indexOf": System.out.println(a.indexOf(value)); break;
                    case "contains": System.out.println(a.contains(value)); break;
                    case "clear": a.clear(); break;
                    case "set": a.set(x, value); break;
                    case "delete": a.delete(x); break;
                }
                
                System.out.printf("%n%d: %s%n%nCommand:", a.size(), Arrays.toString(a.data));
            }
        }
    }

    @Override
    public boolean add(T t) {
        if (count == capacity) {
            return false;
        } else {
            data[(head + count) % capacity] = t;
            count++;
            return true;
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public T remove(T t) {
        int x = this.indexOf(t);
        if (x == 0) return head();
        else if (x == capacity - 1) return tail();
        else return delete(x);
    }

    @Override
    public boolean contains(T t) {
        return this.indexOf(t) >= 0;
    }

    @Override
    public void clear() {
        head = count = 0;
    }

    @Override
    public T get(int i) {
        if (i < 0 || i >= count) throw new IndexOutOfBoundsException();
        return data[(head + i) % capacity];
    }

    @Override
    public boolean set(int i, T t) {
        if (i < 0 || i > count) throw new IndexOutOfBoundsException();
        data[(head + i) % capacity] = t;
        return true;
    }

    @Override
    public T delete(final int i) {
        if (i < 0 || i >= count) throw new NoSuchElementException();
        T t = this.get(i);
        for (int x = i ; x < count - 1 ; x++) {
            this.set(x, this.get(x + 1));
        }
        count--;
        return t;
    }

    @Override
    public int indexOf(T t) {
        for (int i = 0 ; i < this.count ; i++) {
            if (this.get(i).equals(t)) return i;
        }
        return -1;
    }

    @Override
    public T head() {
        if (count == 0) throw new IllegalStateException();
        T t = data[head];
        head = (head + 1) % capacity;
        count--;
        return t;
    }

    @Override
    public T tail() {
        if (count == 0) throw new IllegalStateException();
        T t = data[(head + count - 1) % capacity];
        count--;
        return t;
    }
    
}
