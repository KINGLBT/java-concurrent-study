package com.rebote.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Da Shuai
 * @Date: 2019/7/27 13:32
 * @Description:
 * @Company: zytech
 * @Email: 1043489207@qq.com
 */
public class ReenterLock implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();

    public static int i = 0;

    public void run() {
        for (int j=0;j<1000000;j++) {
            lock.lock();
            try {
                i++;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLock reentrantLock = new ReenterLock();
        Thread t1 = new Thread(reentrantLock);
        Thread t2 = new Thread(reentrantLock);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}
