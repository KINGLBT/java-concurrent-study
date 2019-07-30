package com.rebote.concurrent;

public class ThreadGroupStudy {

    public static class PrintThreadRunnable implements Runnable {

        public void run() {
            String gropAndName = Thread.currentThread().getThreadGroup().getName()+"-"+Thread.currentThread().getName();
            while (true) {
                System.out.println("I am "+gropAndName);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {

        ThreadGroup group = new ThreadGroup("PrintThreadGroup");
        Thread t1 = new Thread(group,new PrintThreadRunnable(),"T1");
        Thread t2 = new Thread(group,new PrintThreadRunnable(),"T2");
        t1.start();
        t2.start();
        // 查看线程组中活跃的线程数
        System.out.println(group.activeCount());
        // 查看线程组中的线程类表
        group.list();

    }

}
