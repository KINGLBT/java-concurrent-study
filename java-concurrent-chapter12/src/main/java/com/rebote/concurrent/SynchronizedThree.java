package com.rebote.concurrent;

public class SynchronizedThree implements Runnable{

    public static int i = 0;

    public static synchronized void increase() {
        i++;
    }

    public void run() {
        for (int j = 0; j < 10000000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new SynchronizedThree());
        Thread t2 = new Thread(new SynchronizedThree());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}