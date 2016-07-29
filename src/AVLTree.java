import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class AVLTree<E extends Comparable<E>>{

    private Node root;
    private int size; // number of nodes

    public AVLTree(){
        this.root = null;
        this.size = 0;
    }

    /**
     * @param data - value to insert
     */
    public void insert(E data) {

        Node current = new Node(data);
        addLeaf(current);
        balance(current);
    }

    /**
     * @param data - value to delete
     * @return: success or not
     */
    public boolean delete(E data) {

        Node current = find(data);
        Node parent = parent(current);

        if (current != null){
            size--;
            deleteNode(data);
            if (parent != null) balance(parent);
            return true;
        }
        return false;
    }

    /**
     * @param data - value to search
     * @return - node if exists
     */
    public Node find(E data){

        if (root != null) {
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


    private void addLeaf(Node current){

        if (root == null) {
            size ++;
            root = current;
        }
        else {
            Node tmp = root;

            if (find((E) current.getData()) == null) {
                while (true) {
                    if (tmp.getData().compareTo(current.getData()) > 0) {
                        if (tmp.getLeft() != null)
                            tmp = tmp.getLeft();
                        else {
                            tmp.setLeft(current);
                            size++;
                            return;
                        }
                    } else if (tmp.getData().compareTo(current.getData()) < 0) {
                        if (tmp.getRight() != null)
                            tmp = tmp.getRight();
                        else {
                            tmp.setRight(current);
                            size++;
                            return;
                        }
                    } else return;
                }
            }
        }
    }

    private void deleteNode(E data){
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

    private void append(Node current, Node child){

        if (parent(current).getData().compareTo(current.getData()) > 0)
            parent(current).setLeft(child);
        else parent(current).setRight(child);
    }


    public void balance(Node current) {

        setBalance(current);
        int balance = current.getBalance();

        // check the balance
        if (balance == -2)
            current = rotateRight(current);

        else if (balance == 2)
            current = rotateLeft(current);
        else {

            // while not root
            Node parent = parent(current);
            if (parent != null)
                balance(parent);
        }
    }

    public Node rotateLeft(Node current) {

        Node y = current.getRight(); // right child
        Node parent = parent(current);

        if (findHeight(current.getRight().getRight()) >= findHeight(current.getRight().getLeft())) {
            Node saved_left_child_of_y = y.getLeft();
            y.setLeft(current);
            current.setRight(saved_left_child_of_y);

            if (parent != null && parent.getData().compareTo(y.getData()) >= 0)
                parent.setLeft(y);
            else if (parent != null)
                parent.setRight(y);
            else
                root = y;

            setBalance(current);
            return y;
        }
        else {
            Node x = y.getLeft();
            Node saved_left_child_of_x = x.getLeft();
            Node saved_right_child_of_x = x.getRight();

            x.setRight(y);
            x.setLeft(current);
            y.setLeft(saved_right_child_of_x);
            current.setRight(saved_left_child_of_x);

            if (parent != null && parent.getData().compareTo(x.getData()) >= 0)
                parent.setLeft(x);
            else if (parent != null)
                parent.setRight(x);
            else root = x;

            setBalance(current);
            setBalance(x);
            return x;
        }
    }

    public Node rotateRight(Node current) {

        Node y = current.getLeft(); // left child
        Node parent = parent(current);

        if (findHeight(current.getLeft().getLeft()) >= findHeight(current.getLeft().getRight())){

            Node saved_right_child_of_y = y.getRight();
            y.setRight(current);
            current.setLeft(saved_right_child_of_y);

            if (parent.getData().compareTo(y.getData()) >= 0)
                parent.setLeft(y);
            else
                parent.setRight(y);

            setBalance(current);
            return y;
        }
        else{
            Node x = y.getRight();
            Node saved_left_child_of_x = x.getLeft();
            Node saved_right_child_of_x = x.getRight();

            x.setLeft(y);
            x.setRight(current);
            y.setRight(saved_left_child_of_x);
            current.setLeft(saved_right_child_of_x);

            if (parent.getData().compareTo(x.getData()) >= 0)
                parent.setLeft(x);
            else
                parent.setRight(x);

            setBalance(current);
            setBalance(x);
            return x;
        }
    }


    private Node parent(Node child) {
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
        }
        else return null;
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


    public int height(){
        return findHeight(root)-1;
    }

    private int findHeight(Node node){
        if (node == null) return 0;
        return 1 + Math.max(findHeight(node.getLeft()), findHeight(node.getRight()));
    }

    private void setBalance(Node current) {
        current.setBalance( findHeight(current.getRight()) - findHeight(current.getLeft()) );
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


    /**
     * Inner class Node
     * @param <E> type of values
     */
    protected class Node<E extends Comparable<E>>{

        protected E data;
        protected Node left;
        protected Node right;
        protected int balance;

        protected Node(E data){
            this.data = data;
            balance = 0;
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

        protected void setBalance(int balance){ this.balance = balance;}      

        protected E getData(){ return this.data;}

        protected Node getLeft(){ return this.left; }

        protected Node getRight(){ return this.right; }

        protected int getBalance(){ return this.balance; }
    }

}


