package com.rebote.concurrent;

public class ThreadRunnable implements Runnable {
    public void run() {
        System.out.println("I am runnable");
    }

    public static void main(String[] args) {
        ThreadRunnable runnable = new ThreadRunnable();
        Thread t2 = new Thread(runnable);
        t2.start();
    }
}
