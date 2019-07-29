package com.rebote.concurrent;

public class ThreadInner {

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                System.out.println("Hello,I am t1");
            }
        });
        t1.start();
    }

}
