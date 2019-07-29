package com.rebote.concurrent;

public class ThreadSleepInterrupt {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("线程中断了");
                        break;
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("InterruptedException When Sleep");
                        // 如果不再次设置中断状态，线程还是不会终止，因为sleep()捕获中断异常，会将中断状态清除
                        Thread.currentThread().interrupt();
                    }

                    System.out.println("我怎么还么有中断?");
                }
            }
        });
        t1.start();
        Thread.sleep(200);
        t1.interrupt();
    }

}
