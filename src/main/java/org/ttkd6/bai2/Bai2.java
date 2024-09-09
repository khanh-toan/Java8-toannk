package org.ttkd6.bai2;

import org.ttkd6.Product;

import java.util.function.Function;

public class Bai2 {
    public static void main(String[] args) {
        // Use apply methods in interface Function
        Function<Integer, Integer> square = x -> x * x;
        System.out.println(square.apply(3));
    }
}
