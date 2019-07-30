package com.rebote.concurrent;

public class GoodSuspend {

    public static Object u = new Object();

    public static class ChangeObjectThread extends Thread {

        public ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in "+getName());
                Thread.currentThread().suspend();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new ChangeObjectThread("t1");
        Thread t2 = new ChangeObjectThread("t2");
        t1.start();
        Thread.sleep(2000);
        t2.start();
        t1.resume();
        Thread.sleep(2000);
        t2.resume();
        t1.join();
        t2.join();
    }


}
