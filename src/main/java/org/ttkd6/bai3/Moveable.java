package org.ttkd6.bai3;

public interface Moveable {
    void eat();
    default void move(){
        System.out.println("I am moving");
    }
}
