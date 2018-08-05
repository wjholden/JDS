package com.wjholden.collections;

import java.util.NoSuchElementException;

abstract class Tree<T extends Comparable> implements Storable<T>, Sortable<T> {

    TreeNode<T> root;

    @Override
    public int size() {
        return TreeNode.size(root);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isSorted() {
        return TreeNode.verifyTreeInvariant(root);
    }

    @Override
    public T min() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return root.leftDescendant().key;
    }

    @Override
    public T max() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return root.rightDescendant().key;
    }
    
    @Override
    public String toString() {
        return String.format("digraph %d {%n%n%s%n}", size(), root);
    }
}
