package com.wjholden.collections;

class SplayNode<T extends Comparable> extends TreeNode<T> {

    public SplayNode(T key) {
        super(key);
    }
    
    public SplayNode(T key, SplayNode<T> parent) {
        super(key, parent);
    }

    SplayNode<T> splay() {
        if (parent == null) {
            return this;
            // do nothing in the base case
        } else if (parent.parent == null) {
            // zig (our parent is root)
            //System.out.print("Zig: ");
            if (key.compareTo(parent.key) < 0) {
                rotateRight(parent);
            } else {
                rotateLeft(parent);
            }
        } else if (key.compareTo(parent.key) * parent.key.compareTo(parent.parent.key) > 0) {
            // The product is positive, indicating that the parents are in the
            // same direction. This is the "zig-zig" case.
            //System.out.print("Zig-Zig: ");
            if (key.compareTo(parent.key) < 0) {
                rotateRight(parent.parent);
                rotateRight(parent);
            } else {
                rotateLeft(parent.parent);
                rotateLeft(parent);
            }
        } else {
            // The product above was positive so this is the "zig-zag" case.
            //System.out.print("Zig-Zag: ");
            if (key.compareTo(parent.key) < 0) {
                rotateRight(parent);
                rotateLeft(parent);
            } else {
                rotateLeft(parent);
                rotateRight(parent);
            }
        }
        return this.splay();
    }
    
}
