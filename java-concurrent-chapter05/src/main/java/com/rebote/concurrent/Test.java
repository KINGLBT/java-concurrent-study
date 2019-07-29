package com.rebote.concurrent;

public class Test {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("线程没有中断？");
                }
            }
        });

        t1.start();
        t1.interrupt();
    }

}
