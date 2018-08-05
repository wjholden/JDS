package com.wjholden.collections;

class TreeNode<T extends Comparable> {

    private static int counter;

    static void rotateLeft(TreeNode x) {
        // Inorder traversal is AxByC with x starting with the greatest height.
        // x gets rotated to the left and gets subtrees A and B while y becomes
        // the highest node with subtrees AxB and C. We do not assume that x
        // is the root of the entire tree and must therefore adjust parent pointers.
        // We also cannot guess whether x was the left or right child of its parent
        // and must therefore look. A and C the left and right children of x and y, 
        // respectively, and need no adjustment. Only subtree B moves from the
        // left subtree of y to the right subtree of x.
        if (x.right == null) {
            throw new RuntimeException("Cannot rotate left when right child is empty");
        }
        //System.out.println("Rotating " + x.key + " to the left");
        final TreeNode y, b, p;
        y = x.right;
        b = y.left;
        p = x.parent;
        
        y.parent = p;
        if (p != null) {
            if (p.left == x) {
                p.left = y;
            } else {
                p.right = y;
            }
        
        }
        
        x.parent = y;
        y.left = x;
        
        x.right = b;
        if (b != null) {
            b.parent = x;
        }
        assert (verifyTreeInvariant(y));
    }

    static void rotateRight(TreeNode x) {
        if (x.left == null) {
            throw new RuntimeException("Cannot rotate right when left child is empty");
        }
        //System.out.println("Rotating " + x.key + " to the right");
        final TreeNode y, b, p;
        y = x.left;
        b = y.right;
        p = x.parent;
        y.parent = p;
        if (p != null) {
            if (p.left == x) {
                p.left = y;
            } else {
                p.right = y;
            }
        }
        x.parent = y;
        y.right = x;
        x.left = b;
        if (b != null) {
            b.parent = x;
        }
        assert (verifyTreeInvariant(y));
    }

    public static boolean verifyTreeInvariant(TreeNode n) {
        if (n == null) {
            return true;
        }
        return (n.left == null ? true : n.key.compareTo(n.left.key) > 0 && verifyTreeInvariant(n.left))
                && (n.right == null ? true : n.key.compareTo(n.right.key) < 0 && verifyTreeInvariant(n.right));
    }

    public static int size(TreeNode n) {
        if (n == null) {
            return 0;
        } else {
            return 1 + size(n.left) + size(n.right);
        }
    }

    protected final T key;
    protected TreeNode<T> parent, left, right;
    private final int id;

    public TreeNode(T key) {
        this.key = key;
        id = counter++;
    }
    
    public TreeNode(T key, TreeNode<T> parent) {
        this(key);
        setParent(parent);
    }
    
    private void setParent(TreeNode<T> parent) {
        this.parent = parent;
        if (key.compareTo(parent.key) < 0) {
            assert(parent.left == null);
            parent.left = this;
        } else {
            assert(parent.right == null);
            parent.right = this;
        }
    }

    @Override
    public String toString() {
        String s = " ";
        if (left != null) {
            s += String.format(" %d:sw -> %d:n;", id, left.id);
            s += String.format(" %d:n -> %d:sw;", left.id, left.parent.id);
        }
        if (right != null) {
            s += String.format(" %d:se -> %d:n;", id, right.id);
            s += String.format(" %d:n -> %d:se;", right.id, right.parent.id);
        }
        s += String.format(" %d [label=\"%s\"];%n", id, key);
        
        if (left != null) s += left;
        if (right != null) s += right;
        
        return s;
    }

    public TreeNode<T> find(T t) {
        if (key.compareTo(t) < 0 && right != null) {
            return right.find(t);
        } else if (t.compareTo(key) < 0 && left != null) {
            return left.find(t);
        }
        return this;
    }
    
    public TreeNode<T> leftDescendant() {
        if (left == null) return this;
        else return left.leftDescendant();
    }

    public TreeNode<T> rightDescendant() {
        if (right == null) return this;
        else return right.rightDescendant();
    }
    
    public TreeNode<T> leftAncestor() {
        TreeNode<T> c = this;
        while (c.parent != null && c == c.parent.left) c = c.parent;
        return c.parent;
    }
    
    public TreeNode<T> rightAncestor() {
        TreeNode<T> c = this;
        while (c.parent != null && c == c.parent.right) c = c.parent;
        return c.parent;
    }
    
    public TreeNode<T> next() {
        TreeNode<T> n = right == null ? rightAncestor() : right.leftDescendant();
        assert(n == null || key.compareTo(n.key) < 0);
        return n;
    }
    
    public TreeNode<T> prev() {
        TreeNode<T> n = left == null ? leftAncestor() : left.rightDescendant();
        assert(n == null || n.key.compareTo(key) < 0);
        return n;
    }
}
