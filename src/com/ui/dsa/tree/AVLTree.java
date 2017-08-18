package com.ui.dsa.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * AVL tree
 *
 * @param <E> - data type of nodes
 */
public class AVLTree<E extends Comparable<E>> {

    private Node root;
    private int size;

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    public void insert(E data) {
        Node current = new Node(data);
        addLeaf(current);
        balance(current);
    }

    public boolean delete(E data) {
        Node current = find(data);
        Node parent = parent(current);
        if (current != null) {
            size--;
            deleteNode(data);
            if (parent != null) balance(parent);
            return true;
        }
        return false;
    }

    public Node find(E data) {
        if (root != null) {
            Node current = root;
            while (true) {
                if (current.getData().compareTo(data) > 0 && current.getLeft() != null) {
                    current = current.getLeft();
                } else if (current.getData().compareTo(data) < 0 && current.getRight() != null) {
                    current = current.getRight();
                } else if (current.getData().compareTo(data) == 0) {
                    return current;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private void addLeaf(Node current) {
        if (root == null) {
            size++;
            root = current;
        } else {
            Node tmp = root;
            if (find((E) current.getData()) == null) {
                while (true) {
                    if (tmp.getData().compareTo(current.getData()) > 0) {
                        if (tmp.getLeft() != null) {
                            tmp = tmp.getLeft();
                        } else {
                            tmp.setLeft(current);
                            size++;
                            return;
                        }
                    } else if (tmp.getData().compareTo(current.getData()) < 0) {
                        if (tmp.getRight() != null) {
                            tmp = tmp.getRight();
                        } else {
                            tmp.setRight(current);
                            size++;
                            return;
                        }
                    } else { return; }
                }
            }
        }
    }

    private void deleteNode(E data) {
        if (size != 0) {
            Node current = find(data);
            if (current != null) {
                // has no children
                if (current.getLeft() == null && current.getRight() == null) {
                    if (parent(current) == null) {
                        root = null;
                    } else {
                        replace(current, null);
                    }
                }
                // has only left child
                else if (current.getLeft() != null && current.getRight() == null) {
                    if (parent(current) == null) {
                        root = current.getLeft();
                    } else {
                        replace(current, current.getLeft());
                    }
                }
                // if only right child
                else if (current.getLeft() == null && current.getRight() != null) {
                    if (parent(current) == null) {
                        root = current.getRight();
                    } else {
                        replace(current, current.getRight());
                    }
                }
                // if the node has 2 children
                else {
                    Node pred = predecessor(current);
                    delete((E) pred.getData());
                    current.setData(pred.getData());
                }
                size--;
            }
        }
    }

    private void replace(Node current, Node child) {
        if (parent(current).getData().compareTo(current.getData()) > 0) {
            parent(current).setLeft(child);
        } else { parent(current).setRight(child); }
    }


    public void balance(Node current) {
        setBalance(current);
        int balance = current.getBalance();
        // check the balance
        if (balance == -2) {
            current = rotateRight(current);
        } else if (balance == 2) {
            current = rotateLeft(current);
        } else {
            // while not root
            Node parent = parent(current);
            if (parent != null) {
                balance(parent);
            }
        }
    }

    public Node rotateLeft(Node current) {
        Node y = current.getRight();
        Node parent = parent(current);
        if (findHeight(current.getRight().getRight()) >= findHeight(current.getRight().getLeft())) {
            Node leftChildOfY = y.getLeft();
            y.setLeft(current);
            current.setRight(leftChildOfY);
            if (parent != null && parent.getData().compareTo(y.getData()) >= 0) {
                parent.setLeft(y);
            } else if (parent != null) {
                parent.setRight(y);
            } else {
                root = y;
            }
            setBalance(current);
            return y;
        } else {
            Node x = y.getLeft();
            Node leftChildOfX = x.getLeft();
            Node saved_right_child_of_x = x.getRight();
            x.setRight(y);
            x.setLeft(current);
            y.setLeft(saved_right_child_of_x);
            current.setRight(leftChildOfX);
            if (parent != null && parent.getData().compareTo(x.getData()) >= 0) {
                parent.setLeft(x);
            } else if (parent != null) {
                parent.setRight(x);
            } else {
                root = x;
            }
            setBalance(current);
            setBalance(x);
            return x;
        }
    }

    public Node rotateRight(Node current) {
        Node y = current.getLeft(); // left child
        Node parent = parent(current);
        if (findHeight(current.getLeft().getLeft()) >= findHeight(current.getLeft().getRight())) {
            Node rightChildOfY = y.getRight();
            y.setRight(current);
            current.setLeft(rightChildOfY);
            if (parent.getData().compareTo(y.getData()) >= 0) {
                parent.setLeft(y);
            } else {
                parent.setRight(y);
            }
            setBalance(current);
            return y;
        } else {
            Node x = y.getRight();
            Node leftChildOfX = x.getLeft();
            Node rightChildOfX = x.getRight();
            x.setLeft(y);
            x.setRight(current);
            y.setRight(leftChildOfX);
            current.setLeft(rightChildOfX);
            if (parent.getData().compareTo(x.getData()) >= 0) {
                parent.setLeft(x);
            } else {
                parent.setRight(x);
            }
            setBalance(current);
            setBalance(x);
            return x;
        }
    }

    private Node parent(Node child) {
        if (root != null) {
            Node current = root;
            while (true) {
                if (current.getLeft() != null && current.getLeft().getData().compareTo(child.getData()) == 0) {
                    return current;
                } else if (current.getRight() != null && current.getRight().getData().compareTo(child.getData()) == 0) {
                    return current;
                } else if (current.getLeft() != null && current.getData().compareTo(child.getData()) > 0) {
                    current = current.getLeft();
                } else if (current.getRight() != null) current = current.getRight();
                else { return null; }
            }
        }
        return null;
    }

    public int getSize() {
        return size;
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


    public int height() {
        return findHeight(root) - 1;
    }

    private int findHeight(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(findHeight(node.getLeft()), findHeight(node.getRight()));
    }

    private void setBalance(Node current) {
        current.setBalance(findHeight(current.getRight()) - findHeight(current.getLeft()));
    }

    public List<E> getLine(int line) {
        List<E> list = new LinkedList<>();
        return getLine(line, 0, root, list);
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
        } else if (currentLine == l) {
            list.add((E) current.getData());
        }
        return list;
    }

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

    protected class Node<E extends Comparable<E>> {
        E data;
        Node left;
        Node right;
        int balance;

        Node(E data) {
            this.data = data;
            balance = 0;
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

        void setBalance(int balance) {
            this.balance = balance;
        }

        E getData() {
            return this.data;
        }

        Node getLeft() {
            return this.left;
        }

        Node getRight() {
            return this.right;
        }

        int getBalance() {
            return this.balance;
        }
    }
}


