package com.rebote.concurrent;

public class SynchronizedOne {

    public static SynchronizedOneRunnable runnable = new SynchronizedOneRunnable();

    public static volatile  int i = 0;

    public static class SynchronizedOneRunnable implements Runnable {

        public void run() {
            for (int j = 0; j < 10000000; j++) {
                synchronized (runnable) {
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}
