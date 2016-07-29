import com.sun.org.apache.xpath.internal.SourceTree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {


    public static void main(String[] args) {

       RBTree<Integer> tree = new RBTree<>();
       long start, finish;

       for (int i = 0; i<999999; i++){

           start = System.nanoTime();
           tree.insert(i);
           finish = System.nanoTime();

           if (i % 50000 == 0)
                System.out.println(i+": "+(finish-start));
       }

    }
}
