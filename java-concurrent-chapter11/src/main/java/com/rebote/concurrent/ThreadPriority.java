package com.rebote.concurrent;

public class ThreadPriority {

    public static class HightPriority extends Thread {

        static int count = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (ThreadPriority.class) {
                    count++;
                    if (count>100000000) {
                        System.out.println("HighPriority is complete");
                    }
                }
            }
        }

    }

    public static class LowPriority extends Thread {

        static int count = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (ThreadPriority.class) {
                    count++;
                    if (count>100000000) {
                        System.out.println("LowPriority is complete");
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        Thread high = new HightPriority();
        Thread low = new LowPriority();
        high.setPriority(Thread.MAX_PRIORITY);
        low.setPriority(Thread.MIN_PRIORITY);
        low.start();
        high.start();
    }

}
