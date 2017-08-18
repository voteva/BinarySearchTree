package com.ui.dsa.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * Binary search tree
 *
 * @param <E> - data type of nodes
 */
public class BSTree<E extends Comparable<E>> {

    private Node root;
    private int size;
    private int height;

    public BSTree() {
        root = null;
        size = 0;
        height = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int getSize() {
        return this.size;
    }

    public void insert(E data) {
        if (isEmpty()) {
            root = new Node(data);
            size++;
        } else {
            Node current = root;
            Node newNode = new Node(data);
            boolean inserted = false;
            while (!inserted) {
                // left direction
                if (current.getData().compareTo(data) > 0) {
                    if (current.getLeft() != null) {
                        current = current.getLeft();
                    } else {
                        current.setLeft(newNode);
                        size++;
                        inserted = true;
                    }
                }
                // right direction
                else if (current.getData().compareTo(data) < 0) {
                    if (current.getRight() != null) {
                        current = current.getRight();
                    } else {
                        current.setRight(newNode);
                        size++;
                        inserted = true;
                    }
                }
                // if the element already exists
                else {
                    inserted = true;
                }
            }
        }
    }

    public void delete(E data) {
        if (!isEmpty()) {
            Node current = find(data);
            if (current != null) {
                if (isLeaf(current)) {
                    // if root
                    if (getParent(current) == null) {
                        root = null;
                    } else {
                        replace(current, null);
                    }
                }
                // has only left child
                else if (current.getLeft() != null && current.getRight() == null) {
                    if (getParent(current) == null) {
                        root = current.getLeft();
                    } else {
                        replace(current, current.getLeft());
                    }
                }
                // has only right child
                else if (current.getLeft() == null && current.getRight() != null) {
                    if (getParent(current) == null) {
                        root = current.getRight();
                    } else {
                        replace(current, current.getRight());
                    }
                }
                // has 2 children
                else {
                    Node pred = predecessor(current);
                    delete((E) pred.getData());
                    current.setData(pred.getData());
                }
                size--;
            }
        }
    }

    public Node find(E data) {
        if (isEmpty()) {
            Node current = root;
            while (true) {
                if (current.getData().compareTo(data) > 0 && current.getLeft() != null) {
                    current = current.getLeft();
                    continue;
                } else if (current.getData().compareTo(data) < 0 && current.getRight() != null) {
                    current = current.getRight();
                    continue;
                } else if (current.getData().compareTo(data) == 0) {
                    return current;
                }
                return null;
            }
        }
        return null;
    }

    public int height() {
        return findHeight(root) - 1;
    }

    public Node predecessor(Node current) {
        current = current.getLeft();
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current;
    }

    public Node successor(Node current) {
        current = current.getRight();
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Finds the minimum value in a tree
     *
     * @return - the minimum found value
     */
    public E min() {
        Node current = root;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return (E) current.getData();
    }

    /**
     * Finds the maximum value in a tree
     *
     * @return - the maximum found value
     */
    public E max() {

        Node current = root;
        while (current.getRight() != null)
            current = current.getRight();

        return (E) current.getData();
    }

    /**
     * Finds all elements in the current line
     *
     * @param line - line with elements
     * @return all nodes in the line
     */
    public List<E> getLine(int line) {
        LinkedList<E> list = new LinkedList<>();
        return getLine(line, 0, root, list);
    }

    /**
     * Tree visualization
     */
    public void visualize() {
        int h = height();
        List<E> list;
        for (int i = 0; i <= h; i++) {
            list = getLine(i);
            for (int j = 0; j < (Math.pow(2, h) - 1) / Math.pow(2, i); j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < Math.pow(2, i); j++) {
                if (list.get(j) == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(list.get(j));
                }
                if (i > 0) {
                    for (int k = 0; k < (Math.pow(2, h + 1) - 1 - Math.pow(2, i) - 2 * ((Math.pow(2, h) - 1) / Math.pow(2, i))) / (Math.pow(2, i) - 1); k++) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

    private int findHeight(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(findHeight(node.getLeft()), findHeight(node.getRight()));
    }

    private Node getParent(Node child) {
        Node parent = root;
        if (parent != null) {
            while (true) {
                if (parent.getLeft() != null) {
                    if (parent.getLeft().getData().compareTo(child.getData()) == 0) {
                        return parent;
                    } else if (parent.getData().compareTo(child.getData()) > 0) {
                        parent = parent.getLeft();
                        continue;
                    }
                }
                if (parent.getRight() != null) {
                    if (parent.getRight().getData().compareTo(child.getData()) == 0) {
                        return parent;
                    } else if (parent.getData().compareTo(child.getData()) > 0) {
                        parent = parent.getRight();
                        continue;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private boolean isLeaf(Node node) {
        return node.getLeft() == null && node.getRight() == null;
    }

    private void replace(Node current, Node child) {
        Node parent = getParent(current);
        if (parent != null) {
            if (parent.getData().compareTo(current.getData()) > 0) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        }
    }

    private List<E> getLine(int l, int currentLine, Node current, List<E> list) {
        if (currentLine < l && current.getLeft() == null && current.getRight() == null) {
            for (int i = 0; i < Math.pow(2, l - currentLine) - 1; i++) {
                list.add(null);
                list.add(null);
            }
        } else if (currentLine < l && current.getLeft() != null && current.getRight() != null) {
            getLine(l, currentLine + 1, current.getLeft(), list);
            getLine(l, currentLine + 1, current.getRight(), list);
        } else if (currentLine < l && current.getLeft() != null && current.getRight() == null) {
            getLine(l, currentLine + 1, current.getLeft(), list);
            for (int i = 0; i < Math.pow(2, l - currentLine) - 1; i++) {
                list.add(null);
            }
        } else if (currentLine < l && current.getLeft() == null && current.getRight() != null) {
            for (int i = 0; i < Math.pow(2, l - currentLine) - 1; i++) {
                list.add(null);
            }
            getLine(l, currentLine + 1, current.getRight(), list);
        } else if (currentLine == l) list.add((E) current.getData());
        return list;
    }

    public class Node<E extends Comparable<E>> {
        E data;
        Node left;
        Node right;

        Node(E data) {
            this.data = data;
            left = null;
            right = null;
        }

        void setData(E data) {
            this.data = data;
        }

        Node setLeft(Node<E> leftNode) {
            if (left != null) {
                this.left = leftNode;
                return null;
            } else {
                Node tmp = this.left;
                this.left = leftNode;
                return tmp;
            }
        }

        Node setRight(Node<E> rightNode) {
            if (left != null) {
                this.right = rightNode;
                return null;
            } else {
                Node tmp = this.right;
                this.right = rightNode;
                return tmp;
            }
        }

        Node getLeft() {
            return this.left;
        }

        Node getRight() {
            return this.right;
        }

        E getData() {
            return this.data;
        }
    }
}
