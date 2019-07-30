package com.rebote.concurrent;

public class VolidateThread {

    public static volatile  int i = 0;

    public static class VolidateRunnable implements Runnable {

        public void run() {
            for (int j = 0; j < 100000000; j++) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolidateRunnable runnable = new VolidateRunnable();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}
