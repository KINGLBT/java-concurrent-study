package com.rebote.concurrent;

public class DaemonThread {

    public static class DaemonT extends Thread {

        @Override
        public void run() {
            while (true) {
                System.out.println("I am alive");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread();
        t.setDaemon(true);
        t.start();

        Thread.sleep(2000);

    }

}
