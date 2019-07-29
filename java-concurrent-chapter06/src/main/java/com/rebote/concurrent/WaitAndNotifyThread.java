package com.rebote.concurrent;

public class WaitAndNotifyThread {

    public static Object object = new Object();

    public static class  WaitThread extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis()+": WaitThread start");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis()+": WaitThread end");
            }
        }
    }

    public static class NotifyThread extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis()+": NotifyThread start");
                object.notify();
                System.out.println(System.currentTimeMillis()+": NotifyThread end");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new WaitThread();
        t1.start();
        Thread t2 = new NotifyThread();
        t2.start();
    }
}
