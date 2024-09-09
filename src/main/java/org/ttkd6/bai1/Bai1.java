package org.ttkd6.bai1;

import java.util.ArrayList;
import java.util.List;

public class Bai1{
    public static void main(String[] args) {
        // Use lambda expression
        //Operator<Integer> addOperation = (a, b) -> a + b;

        //Use anonymous class
        Operator<Integer> addAnonymous = new Operator<Integer>() {
            @Override
            public Integer process(Integer a, Integer b) {
                return a + b;
            }
        };
        System.out.println(addAnonymous.process(3, 4));

        // Use method reference
        Operator<Integer> addOperation = Integer::sum;
        System.out.println(addOperation.process(3, 3));

        Operator<String> appendOperation = (a, b) -> a + b;
        System.out.println(appendOperation.process("3", "3"));

        // Don't have return in the expression
        //Operator<String> throwsClause = (a, b) -> return a * b;

        List<String> pointList = new ArrayList<>();

        pointList.add("1");
        pointList.add("2");

        pointList.forEach(p -> System.out.println(p));

        new Thread(() -> System.out.println("My Runnable")).start();
    }
}
