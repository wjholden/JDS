package com.wjholden.collections;

import java.util.Scanner;

public class AVLTreeSet<T extends Comparable<T>> implements Storable<T>, Sortable<T> {
    private static int counter = 0;

    private static int height(AVLTreeSet tree) {
        if (tree == null) return 0;
        else return tree.height;
    }
    
    private static int count(AVLTreeSet tree) {
        if (tree == null) return 0;
        else return tree.count;
    }
    
    private static void rebalance(AVLTreeSet n) {
        AVLTreeSet p = n.parent;
        
        if (p != null) { // Never rebalance the dummy root node.
            if (height(n.left) > height(n.right) + 1) rebalanceRight(n);
            else if (height(n.right) > height(n.left) + 1) rebalanceLeft(n);
            rebalance(p);
        }
    }
    
    private static void rebalanceLeft(AVLTreeSet n) {
        AVLTreeSet m = n.right;
        if (m != null && height(m.left) > height(m.right)) {
            rotateRight(m);
            m.setHeight();
        }
        rotateLeft(n);
        n.setHeight();
    }
    
    private static void rebalanceRight(AVLTreeSet n) {
        AVLTreeSet m = n.left;
        if (m != null && height(m.right) > height(m.left)) {
            rotateLeft(m);
        }
        rotateRight(n);
    }
    
    private static void rotateLeft(AVLTreeSet x) {
        //System.out.println("Rotating left on " + x.key);
        if (x.right == null) throw new RuntimeException(
                "Could not rotate on " + x.key + " because right child is null");
        final AVLTreeSet p, y, b;
        p = x.parent;
        y = x.right;
        b = y.left;
        if (p != null) {
            if (p.left == x) p.left = y;
            else p.right = y;
        }
        y.parent = p;
        x.parent = y;
        y.left = x;
        x.right = b;
        if (b != null) b.parent = x;
        
        x.setHeight();
    }
    
    private static void rotateRight(final AVLTreeSet x) {
        //System.out.println("Rotating right on " + x.key);
        if (x.left == null) throw new RuntimeException(
                "Could not rotate on " + x.key + " because left child is null");
        final AVLTreeSet p, y, b;
        p = x.parent;
        y = x.left;
        b = y.right;
        if (p != null) {
            if (p.left == x) p.left = y;
            else p.right = y;
        }
        y.parent = p;
        x.parent = y;
        y.right = x;
        x.left = b;
        if (b != null) {
            b.parent = x;
        }
        
        x.setHeight();
    }
    
    public static void main(String args[]) {
        AVLTreeSet<String> a = new AVLTreeSet<>();
        
        try (Scanner s = new Scanner(System.in)) {
            System.out.print("Command: " );
            while (s.hasNext()) {
                String command = s.next();
                switch (command) {
                    case "add": System.out.println(a.add(s.next())); break;
                    case "remove": System.out.println(a.remove(s.next())); break;
                    case "contains": System.out.println(a.contains(s.next())); break;
                    case "clear": a.clear(); break;
                    case "show": System.out.println(a.toString()); break;
                    case "size": System.out.println(a.size()); break;
                    case "find": System.out.println(a.find(s.next()).key); break;
                    case "next": System.out.println(a.successor(s.next())); break;
                    case "prev": System.out.println(a.predecessor(s.next())); break;
                    case "min": System.out.println(a.min()); break;
                    case "max": System.out.println(a.max()); break;
                    case "isSorted": System.out.println(a.isSorted()); break; 
                    case "subset": System.out.println(a.subset(s.next(), s.next())); break;
                    case "quit": return;
                    case "rem": s.nextLine(); break;
                    default: System.err.println("Command " + command + " not recognized.");
                }
                System.out.print("Command: ");
            }
        }
    }
    
    private AVLTreeSet<T> left, right, parent;
    private T key;
    private int count, height;
    private final int id;
    
    public AVLTreeSet() {
        // The root node is a dummy with no value in the key. It is never rotated.
        id = counter;
        counter++;
    }
    
    private AVLTreeSet(T key, AVLTreeSet<T> parent) {
        this();
        assert(key != null);
        this.key = key;
        count = height = 1;
        assert(parent != this);
        this.parent = parent;
    }
    
    private void setHeight() {
        this.height = 1 + Math.max(height(left), height(right));
        this.count = (key == null ? 0 : 1) + count(left) + count(right);
        if (parent != null) parent.setHeight();
    }
    
    private AVLTreeSet<T> find(T key) {
        if (this.key == null) {
            return (left == null) ? this : left.find(key);
        } else if (this.key.compareTo(key) > 0 && left != null) {
            return left.find(key);
        } else if (this.key.compareTo(key) < 0 && right != null) {
            return right.find(key);
        } else {
            return this; // either we found a match or we found its parent.
        }
    }
    
    @Override
    public boolean add(T t) {
        if (t == null) throw new NullPointerException("Cannot add null element to AVL tree");
        
        AVLTreeSet<T> n = this.find(t);
        assert(n != null);
        if (n.key == null) {
            n.left = new AVLTreeSet<>(t, n);
            n.left.setHeight();
        } else if (n.key.equals(t)) {
            return false; // key already present in AVL tree
        } else if (n.key.compareTo(t) > 0) {
            assert(n.left == null);
            n.left = new AVLTreeSet<>(t, n);
            n.left.setHeight(); // must be done after above assignment is complete
        } else {
            assert(n.right == null);
            n.right = new AVLTreeSet<>(t, n);
            n.right.setHeight();
        }
        
        rebalance(n);
        return true;
    }

    @Override
    public int size() {
        return count(this);
    }

    @Override
    public T remove(T t) {
        AVLTreeSet<T> n = this.find(t);
        if (n.right == null) {
            replace(n, n.left);
        } else {
            final AVLTreeSet<T> x = find(successor(t)), y = x.right, m = x.parent;
            replace(x, y);
            replace(n, x);
            m.setHeight();
            rebalance(m);
        }
        return n.key;
    }
    
    private static void replace(AVLTreeSet me, AVLTreeSet with) {
        if (me == null) throw new NullPointerException("Cannot replace a null node");
        
        if (with == null) {
            if (me.parent.left == me) me.parent.left = null;
            else me.parent.right = null;
            return;
        }
        
        with.left = me.left;
        with.right = me.right;
        with.parent = me.parent;
        
        if (with.left != null) with.left.parent = with;
        if (with.right != null) with.right.parent = with;
        if (with.parent != null) {
            if (with.parent.left == me) with.parent.left = with;
            else with.parent.right = with;
        }
    }
    
    @Override
    public boolean contains(T t) {
        return t.equals(this.find(t).key);
    }

    @Override
    public void clear() {
        left = right = null;
        assert(key == null);
        count = height = 0;
    }

    @Override
    public boolean isSorted() {
        if (this.size() < 2) return true;
        else if (this.key == null) return left.isSorted();
        else return (left == null ? true :
                    left.key.compareTo(this.key) < 0 && left.isSorted()) &&
                (right == null ? true :
                    this.key.compareTo(right.key) < 0 && right.isSorted());
    }

    @Override
    public T min() {
        return left == null ? this.key : left.min();
    }

    @Override
    public T max() {
        if (this.key == null && this.left != null) return left.max();
        
        return right == null ? this.key : right.max();
    }

    private static AVLTreeSet leftDescendant(AVLTreeSet n) {
        AVLTreeSet d = n;
        while (d.left != null) d = d.left;
        return d;
    }
    
    private static AVLTreeSet rightDescendant(AVLTreeSet n) {
        AVLTreeSet d = n;
        while (d.right != null) d = d.right;
        return d;
    }
    
    private static AVLTreeSet leftAncestor(AVLTreeSet n) {
        AVLTreeSet a = n;
        while (a.parent.left == a) a = a.parent;
        return a.parent;
    }
    
    private static AVLTreeSet rightAncestor(AVLTreeSet n) {
        AVLTreeSet a = n;
        // Continue going up as long as I am my parent's right child.
        // When I find that I am my parent's left child this means that
        // my parent is greater than me and, transitively, the successor
        // value to the original value I was looking for.
        while (a.parent.right == a) a = a.parent;
        return a.parent;
    }
    
    @Override
    public T successor(T t) {
        AVLTreeSet<T> n = this.find(t);
        if (this.size() == 0) {
            return null;
        } else if (t.compareTo(this.max()) >= 0) {
            return null;
        } else if (this.size() == 1) {
            return this.left.key; // a dummy root was probably a bad idea.
        } else if (t.compareTo(n.key) >= 0) {
            return (T) (n.right == null ? rightAncestor(n) : leftDescendant(n.right)).key;
        } else {
            return n.key;
        }
    }

    @Override
    public T predecessor(T t) {
        AVLTreeSet<T> n = this.find(t);
        if (this.size() == 0) {
            return null;
        } else if (t.compareTo(this.min()) <= 0) {
            return null;
        } else if (this.size() == 1) {
            return this.left.key;
        } else if (t.compareTo(n.key) <= 0) {
            return (T) (n.left == null ? leftAncestor(n): rightDescendant(n.left)).key;
        } else {
            return n.key;
        }
    }

    @Override
    public String toString() {
        String s = "";
        if (this.parent == null) {
            s += "digraph {\n";
        }
        s += String.format("  %d [label=\"%s\"];%n", id, key);
        if (left != null) {
            s += String.format("  %d:sw -> %d:n [label=\"%d\"];%n", this.id, left.id, left.height);
            //s += String.format("  %d:n -> %d:sw [label=\"%d\"];%n", left.id, left.parent.id, left.height);
            s += left.toString();
        }
        if (right != null) {
            s += String.format("  %d:se -> %d:n [label=\"%d\"];%n", this.id, right.id, right.height);
            //s += String.format("  %d:n -> %d:se [label=\"%d\"];%n", right.id, right.parent.id, right.height);
            s += right.toString();
        }
        if (this.parent == null) {
            s += "}";
        }
        return s;
    }

    @Override
    public Sortable<T> subset(final T first, final T last) {
        if (first.compareTo(last) > 0)
            throw new IllegalArgumentException("Subset range given in decreasing order");
        final AVLTreeSet<T> a = new AVLTreeSet<>();

        if (this.size() > 1) {
            // The current position is initialied as the "first" value, if present
            // in the tree, or its successor (if any). If "first" is greater than
            // all values in the tree then current == null and the loop will be skipped.
            T current = this.contains(first) ? first : this.successor(first);
            while (current != null && current.compareTo(last) <= 0) {
                a.add(current);
                current = this.successor(current);
            }
        } else if (this.size() == 1 && (this.contains(first) || this.contains(last))) {
            // Trickiness around case of tree of size 1. The previous case does
            // not handle this case correctly 
            if (first.compareTo(left.key) <= 0 && left.key.compareTo(last) <= 0)
                a.add(left.key);
        }
        // we implicitly bypass but correctly handle the case of tree of size 0.
        
        return a;
    }
}
