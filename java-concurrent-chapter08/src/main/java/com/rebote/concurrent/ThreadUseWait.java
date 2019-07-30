package com.rebote.concurrent;

public class ThreadUseWait {

    public static Thread t1 = new Thread1();

    public static class Thread1 extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Thread2 extends Thread {

        @Override
        public void run() {
            synchronized (t1) {
                System.out.println("T2获取到t1监听器");
                try {
                    System.out.println("T2线程进行等待，释放资源");
                    t1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T2被唤醒了，继续执行");
            }
        }
    }


    public static class Thread3 extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (t1) {
                System.out.println("T3获取到t1监听器");
                System.out.println("T3线程进行线程唤醒，释放资源");
                t1.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t2 = new Thread2();
        Thread t3 = new Thread3();
        t1.start();
        t2.start();
        t3.start();
        t1.join();
    }
}
