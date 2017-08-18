package com.ui.dsa.tree;

import com.ui.dsa.tree.enumerations.RBTreeColor;

import java.util.LinkedList;
import java.util.List;

import static com.ui.dsa.tree.enumerations.RBTreeColor.BLACK;
import static com.ui.dsa.tree.enumerations.RBTreeColor.RED;

/**
 * Red black tree
 *
 * @param <E> - data type of nodes
 */
public class RBTree<E extends Comparable<E>> {

    private Node root;
    private int size;

    public RBTree() {
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(E data) {
        Node current = new Node(data);
        // case 1: if root
        if (root == null) {
            root = current;
            root.setColor(BLACK);
            size++;
        } else if (find(data) == null) {
            addLeaf(current);
            // case 2: tree is still valid
            if (getParent(current).getColor() == BLACK) {
                // do nothing
            } else {
                Node uncle = getUncle(current);
                Node parent = getParent(current);
                Node gp = getGrandparent(current);
                // case 3: avoid 2 consecutive red nodes
                if ((uncle != null) && (uncle.getColor() == RED) && (parent.getColor() == (RED))) {
                    parent.setColor(BLACK);
                    uncle.setColor(BLACK);
                    if (gp != root) {
                        gp.setColor(RED);
                    }
                }
                // case 4: rotation
                else {
                    if ((current == parent.getRight()) && (parent == gp.getLeft())) {
                        rotateLeft(current);
                        current = current.getLeft();
                    } else if ((current == parent.getLeft()) && (parent == gp.getRight())) {
                        rotateRight(current);
                        current = current.getRight();
                    }
                    gp = getGrandparent(current);
                    parent = getParent(current);
                    parent.setColor(BLACK);
                    gp.setColor(RED);
                    if ((current == parent.getLeft()) && (parent == gp.getLeft())) {
                        rotateRight(parent);
                    } else {
                        rotateLeft(parent);
                    }
                }
            }
        }
    }

    public boolean delete(E data) {
        Node current = find(data);
        if (current == null) {
            return false;
        }
        if (current.getLeft() == null && current.getRight() == null) {
            deleteNode((E) current.getData());
            return true;
        }
        Node child;
        if (current.getRight() == null) {
            child = current.getLeft();
        } else {
            child = current.getRight();
        }
        current.setData(child.getData());
        deleteNode((E) child.getData());
        if (current.getColor() == BLACK) {
            if (child.getColor().equals(RED)) {
                child.setColor(BLACK);
            }
        } else {
            //case 1
            if (getParent(current) != null) {
                Node parent = getParent(current);
                Node sibling = getSibling(current);
                // case 2
                if (sibling.getColor() == RED) {
                    parent.setColor(RED);
                    sibling.setColor(BLACK);
                    if (current == parent.getLeft()) {
                        rotateLeft(parent);
                    } else {
                        rotateRight(parent);
                    }
                }
                //case 3
                sibling = getSibling(current);
                if ((parent.getColor().equals(BLACK))
                        && (sibling.getColor().equals(BLACK))
                        && (sibling.getLeft().getColor().equals(BLACK))
                        && (sibling.getRight().getColor().equals(BLACK))) {
                    sibling.setColor(RED);
                    delete((E) getParent(current).getData());
                } else {
                    //case 4
                    if ((getParent(current).getColor() == RED)
                            && (sibling.getColor() == BLACK)
                            && (sibling.getLeft().getColor() == BLACK)
                            && (sibling.getRight().getColor() == BLACK)) {
                        sibling.setColor(RED);
                        getParent(current).setColor(BLACK);
                    } else {
                        //case 5
                        if (sibling.getColor() == BLACK) {
                            if ((current == getParent(current).getLeft())
                                    && (sibling.getRight().getColor() == BLACK)
                                    && (sibling.getLeft().getColor() == RED)) {
                                sibling.setColor(RED);
                                sibling.getLeft().setColor(BLACK);
                                rotateRight(sibling);
                            } else if ((current == getParent(current).getRight())
                                    && (sibling.getLeft().getColor() == BLACK)
                                    && (sibling.getRight().getColor() == RED)) {
                                sibling.setColor(RED);
                                sibling.getRight().setColor(BLACK);
                                rotateLeft(sibling);
                            }
                        }
                        //case 6
                        sibling = getSibling(current);
                        sibling.setColor(getParent(current).getColor());
                        getParent(current).setColor(BLACK);
                        if (current == getParent(current).getLeft()) {
                            sibling.getRight().setColor(BLACK);
                            rotateLeft(getParent(sibling));
                        } else {
                            sibling.getLeft().setColor(BLACK);
                            rotateRight(getParent(current));
                        }
                    }
                }
            }
            deleteNode((E) current.getData());
        }

        return true;
    }

    private void deleteNode(E data) {
        if (!isEmpty()) {
            Node current = find(data);
            if (current != null) {
                // if no children
                if (current.getLeft() == null && current.getRight() == null) {
                    if (getParent(current) == null) {
                        root = null;
                    } else {
                        replace(current, null);
                    }
                }
                // if only left child
                else if (current.getLeft() != null && current.getRight() == null) {
                    if (getParent(current) == null) {
                        root = current.getLeft();
                    } else {
                        replace(current, current.getLeft());
                    }
                }
                // if only right child
                else if (current.getLeft() == null && current.getRight() != null) {
                    if (getParent(current) == null) {
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

    private void rotateLeft(Node current) {
        Node gp = getGrandparent(current);
        Node leftChild = current.getLeft();
        Node parent;
        if (gp == null) {
            parent = getParent(current);
            root = current;
        } else if (gp.getData().compareTo(current.getData()) > 0) {
            parent = gp.getLeft();
            gp.setLeft(current);
        } else {
            parent = gp.getRight();
            gp.setRight(current);
        }
        current.setLeft(parent);
        parent.setRight(leftChild);
    }

    private void rotateRight(Node current) {
        Node gp = getGrandparent(current);
        Node rightChild = current.getRight();
        Node parent;
        if (gp == null) {
            parent = getParent(current);
            root = current;
        } else if (gp.getData().compareTo(current.getData()) > 0) {
            parent = gp.getLeft();
            gp.setLeft(current);
        } else {
            parent = gp.getRight();
            gp.setRight(current);
        }
        current.setRight(parent);
        parent.setLeft(rightChild);
    }

    private void addLeaf(Node current) {
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
                } else return;
            }
        }
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

    public Node predecessor(Node current) {
        current = current.getLeft();
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current;
    }

    private Node getParent(Node child) {
        if (root != null) {
            Node current = root;
            while (true) {
                if (current.getLeft() != null && current.getLeft().getData().compareTo(child.getData()) == 0) {
                    return current;
                } else if (current.getRight() != null && current.getRight().getData().compareTo(child.getData()) == 0) {
                    return current;
                } else if (current.getLeft() != null && current.getData().compareTo(child.getData()) > 0) {
                    current = current.getLeft();
                } else if (current.getRight() != null) {
                    current = current.getRight();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private Node getGrandparent(Node current) {
        if ((current != null) && (getParent(current) != null)) {
            return getParent(getParent(current));
        }
        return null;
    }

    private Node getUncle(Node current) {
        Node gp = getGrandparent(current);
        if (gp == null) {
            return null; // no getGrandparent means no getUncle
        }
        if (getParent(current) == gp.getLeft()) {
            return gp.getRight();
        } else {
            return gp.getLeft();
        }
    }

    private Node getSibling(Node current) {
        Node parent = getParent(current);
        if (parent != null && parent.getLeft() == current) {
            return parent.getRight();
        } else if (parent != null) {
            return parent.getLeft();
        } else {
            return null;
        }
    }

    public int size() {
        return size;
    }

    private void replace(Node current, Node child) {
        if (getParent(current).getData().compareTo(current.getData()) > 0)
            getParent(current).setLeft(child);
        else getParent(current).setRight(child);
    }

    public int height() {
        return findHeight(root) - 1;
    }

    private int findHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(findHeight(node.getLeft()), findHeight(node.getRight()));
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
        LinkedList<E> list;
        for (int i = 0; i <= h; i++) {
            list = (LinkedList<E>) getLine(i);
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


    public class Node<E extends Comparable<E>> {
        E data;
        Node left;
        Node right;
        RBTreeColor color;

        Node(E data) {
            this.data = data;
            left = null;
            right = null;
            this.color = RED;
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

        void setColor(RBTreeColor color) {
            this.color = color;
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

        RBTreeColor getColor() {
            return this.color;
        }
    }
}
