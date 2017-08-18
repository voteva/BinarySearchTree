package com.ui.dsa;

import com.ui.dsa.tree.RBTree;

public class Main {

    public static void main(String[] args) {
       RBTree<Integer> tree = new RBTree<>();
       long start, finish;
       for (int i = 0; i < 999999; i++){
           start = System.nanoTime();
           tree.insert(i);
           finish = System.nanoTime();
           if (i % 50000 == 0)
                System.out.println(i + ": " + (finish - start));
       }
    }
}
