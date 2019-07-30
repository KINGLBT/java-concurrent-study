package com.rebote.concurrent;

public class ThreadJoin {

    public static int i = 0;

    public static class AddThread extends Thread {

        @Override
        public void run() {
            for (int j = 0; j < 10000; j++) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new AddThread();
        t1.start();
        t1.join();
        System.out.println(i);
    }

}
