package com.rebote.concurrent;

public class SynchronizedTwo implements Runnable{

    public static int i = 0;

    public synchronized void increase() {
        i++;
    }

    public void run() {
        for (int j = 0; j < 10000000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTwo runnable = new SynchronizedTwo();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}
