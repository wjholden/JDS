package com.wjholden.collections;

import java.util.Scanner;

public class SplayTreeSet<T extends Comparable> extends Tree<T> {

    public static void main(String args[]) {
        final SplayTreeSet<String> a = new SplayTreeSet<>();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Command: ");
            while (scanner.hasNext()) {
                final String command = scanner.next();
                switch (command) {
                    case "add":
                        System.out.println(a.add(scanner.next()));
                        break;
                    case "remove":
                        System.out.println(a.remove(scanner.next()));
                        break;
                    case "contains":
                        System.out.println(a.contains(scanner.next()));
                        break;
                    case "show":
                        System.out.println(a);
                        break;
                    case "quit":
                        return;
                    case "clear":
                        a.clear();
                    case "size":
                        System.out.println(a.size());
                        break;
                    case "min":
                        System.out.println(a.min());
                        break;
                    case "max":
                        System.out.println(a.max());
                        break;
                    case "prev":
                        System.out.println(a.predecessor(scanner.next()));
                        break;
                    case "next":
                        System.out.println(a.successor(scanner.next()));
                        break;
                    default:
                        scanner.nextLine();
                        System.err.println("Unrecognized command " + command);
                }
                System.out.print("Command: ");
            }
        }
    }

    @Override
    public boolean add(T t) {
        assert(root == null || root.parent == null);
        if (root == null) {
            root = new SplayNode<>(t);
            return true;
        } else {
            final SplayNode<T> n = (SplayNode<T>) root.find(t);
            if (n.key.equals(t)) {
                return false;
            } else {
                SplayNode<T> x = new SplayNode<>(t, n);
                root = x.splay();
                return true;
            }
        }
    }

    @Override
    public T remove(T t) {
        assert(root == null || root.parent == null);
        final SplayNode<T> successor, me, left;
        if (!contains(t)) return null;
        
        me = (SplayNode<T>) root.find(t);
        successor = (SplayNode<T>) me.next();
        
        if (successor != null) root = successor.splay(); // move my successor to the root
        root = me.splay(); // I am now the root. My successor is my right child.
        left = (SplayNode<T>) me.left;
        
        assert(root == me);
        assert(me.right == null || (me.right.parent == me && me.right == me.next() && me.right.left == null));
        assert(me.left == null || (me.left.parent == me));
        
        if (successor != null) {
            successor.left = left;
            successor.parent = null;
        }
        if (left != null) left.parent = successor;
        
        root = (successor != null) ? successor : left;
        return me.key;
    }

    @Override
    public boolean contains(T t) {
        if (root == null) {
            return false;
        }
        assert(root == null || root.parent == null);
        root = ((SplayNode<T>) root.find(t)).splay();
        return root.key.equals(t);
    }

    @Override
    public T successor(T t) {
        if (root == null || t.equals(max())) {
            return null;
        } else {
            return root.find(t).next().key;
        }
    }

    @Override
    public T predecessor(T t) {
        if (root == null || t.equals(min())) {
            return null;
        } else {
            return root.find(t).prev().key;
        }
    }

    @Override
    public Sortable<T> subset(T first, T last) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
