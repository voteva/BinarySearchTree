import java.util.LinkedList;
import java.util.Stack;
import java.math.*;

public class BSTree<E extends Comparable<E>> {

    protected Node root;
    protected int size; // number of nodes
    protected int height;

    public BSTree(){
        root = null;
        size = 0;
        height = 0;
    }

    /**
     * @param data - value to add to the tree
     */
    public void insert(E data){

        if (size == 0) {
            root = new Node(data);
            size++;
        }
        else{
            Node current = root;
            Node newNode = new Node(data);

            while (true) {

                // left direction
                if (current.getData().compareTo(data) > 0){
                    if (current.getLeft() != null)
                        current = current.getLeft();
                    else {
                        current.setLeft(newNode);
                        size++;
                        return;
                    }
                }
                //right direction
                else if (current.getData().compareTo(data) < 0) {
                    if (current.getRight() != null)
                        current = current.getRight();
                    else {
                        current.setRight(newNode);
                        size++;return;
                    }
                }
                // if the element already exists
                else return;
            }
        }
    }

    /**
     * @param data - value to remove
     */
    public void delete(E data){

        if (size != 0){
            Node current = find(data);
            if (current != null){

                if (current.getLeft() == null && current.getRight() == null) // if no children
                    if (parent(current) == null) root = null;
                    else append(current, null);

                    // if only left child
                else if (current.getLeft() != null && current.getRight() == null) {
                    if (parent(current) == null) root = current.getLeft();
                    else append(current, current.getLeft());
                }

                // if only right child
                else if (current.getLeft() == null && current.getRight() != null) {
                    if (parent(current) == null) root = current.getRight();
                    else append(current, current.getRight());
                }

                // if the node has 2 children
                else{
                    Node pred = predecessor(current);
                    delete((E) pred.getData());
                    current.setData(pred.getData());
                }

                size--;
            }
        }
    }

    /**
     * @param data - value to search
     * @return - node if exists
     */
    public Node find(E data){

        if (size != 0) {
            Node current = root;
            while (true) {

                if (current.getData().compareTo(data) > 0 && current.getLeft() != null)
                    current = current.getLeft();
                else if (current.getData().compareTo(data) < 0 && current.getRight() != null)
                    current = current.getRight();
                else if (current.getData().compareTo(data) == 0)
                    return current;
                else
                    return null;
            }
        }
        else return null;
    }

    public int height(){
        return findHeight(root)-1;
    }

    private int findHeight(Node node){
        if (node == null) return 0;
        return 1 + Math.max(findHeight(node.getLeft()), findHeight(node.getRight()));
    }

    private Node parent(Node child){
        if (root != null) {
            Node current = root;

            while (true) {
                if (current.getLeft() != null && current.getLeft().getData().compareTo(child.getData()) == 0)
                    return current;
                else if (current.getRight() != null && current.getRight().getData().compareTo(child.getData()) == 0)
                    return current;
                else if (current.getLeft() != null && current.getData().compareTo(child.getData()) > 0)
                    current = current.getLeft();
                else if (current.getRight() != null) current = current.getRight();
                else return null;
            }
        }else
            return null;

    }

    private void append(Node current, Node child){

        if (parent(current).getData().compareTo(current.getData()) > 0)
            parent(current).setLeft(child);
        else parent(current).setRight(child);
    }

    public int size(){
        return size;
    }


    public Node predecessor(Node current){
        current = current.getLeft();
        while (current.getRight() != null)
            current = current.getRight();

        return current;
    }

    public Node successor(Node current){
        current = current.getRight();
        while (current.getLeft() != null)
            current = current.getLeft();

        return current;
    }


    /**
     * Minimal value in a tree
     * @return - the smallest value in the tree
     */
    public E min(){
        Node current = root;
        while (current.getLeft() != null)
            current = current.getLeft();

        return (E)current.getData();
    }

    /**
     * Maximum value in a tree
     * @return - the biggest value in the tree
     */
    public E max(){

        Node current = root;
        while (current.getRight() != null)
            current = current.getRight();

        return (E)current.getData();
    }

    /**
     * All elements in the current line
     * @param line - line with elements
     * @return all nodes in the line
     */
    public LinkedList<E> getLine(int line){
        LinkedList<E> list = new LinkedList<>();
        return getLine(line, 0, root, list);
    }

    private LinkedList<E> getLine(int l, int currentLine, Node current, LinkedList<E> list){

        if (currentLine < l && current.getLeft() == null && current.getRight() == null)
            for (int i = 0; i < Math.pow(2, l - currentLine) - 1; i++) {
                list.add(null);
                list.add(null);
            }

        else if (currentLine < l && current.getLeft() != null && current.getRight() != null){
            getLine(l, currentLine+1, current.getLeft(), list);
            getLine(l, currentLine+1, current.getRight(), list);
        }
        else if (currentLine < l && current.getLeft() != null && current.getRight() == null) {
            getLine(l, currentLine+ 1, current.getLeft(), list);

            for (int i = 0; i < Math.pow(2, l - currentLine) - 1; i++)
                list.add(null);
        }
        else if (currentLine < l && current.getLeft() == null && current.getRight() != null) {
            for (int i = 0; i < Math.pow(2, l - currentLine) - 1; i++)
                list.add(null);

            getLine(l, currentLine+1, current.getRight(), list);
        }else if (currentLine == l) list.addLast((E) current.getData());

        return list;
    }

    public void visualize(){

        int h = height();
        LinkedList<E> list;
        for (int i = 0; i <= h; i++){

            list = getLine(i);
            for(int j = 0; j < (Math.pow(2, h) - 1) / Math.pow(2, i); j++)
                System.out.print(" ");

            for (int j = 0; j < Math.pow(2, i); j++){
                if (list.get(j) == null) System.out.print(" ");
                else System.out.print(list.get(j));

                if (i > 0)
                    for (int k = 0; k < (Math.pow(2, h+1) - 1 - Math.pow(2, i) - 2*((Math.pow(2, h) - 1) / Math.pow(2, i)))/(Math.pow(2, i) - 1); k++)
                        System.out.print(" ");
            }
            System.out.println();
        }
    }


    public class Node<E extends Comparable<E>>{

        protected E data;
        protected Node left;
        protected Node right;

        protected Node(E data){
            this.data = data;
            left = null;
            right = null;
        }

        protected void setData(E data){ this.data = data; }

        protected Node setLeft(Node<E> leftNode){

            if (left != null){
                this.left = leftNode;
                return null;
            }
            else {
                Node tmp = this.left;
                this.left = leftNode;
                return tmp;
            }
        }

        protected Node setRight(Node<E> rightNode){

            if (left != null){
                this.right = rightNode;
                return null;
            }
            else {
                Node tmp = this.right;
                this.right = rightNode;
                return tmp;
            }
        }

        public Node getLeft(){ return this.left; }

        public Node getRight(){ return this.right; }

        public E getData(){ return this.data;}

    }
}
