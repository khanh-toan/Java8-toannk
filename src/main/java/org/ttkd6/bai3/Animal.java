package org.ttkd6.bai3;

public class Animal implements Moveable{
    public static void main(String[] args){
        Animal tiger = new Animal();
        tiger.move();
        tiger.eat();
    }

    @Override
    public void eat() {
        System.out.println("ăn cám ");
    }

    // Nếu không ghi đề thì sẽ được lấy trong default
    /*@Override
    public void move() {
        System.out.println("I am running");
    }*/

}
