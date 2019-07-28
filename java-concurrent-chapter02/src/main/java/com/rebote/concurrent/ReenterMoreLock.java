package com.rebote.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Da Shuai
 * @Date: 2019/7/27 13:56
 * @Description:
 * @Company: zytech
 * @Email: 1043489207@qq.com
 */
public class ReenterMoreLock implements Runnable{

    public static ReentrantLock lock = new ReentrantLock();

    public static int i = 0;

    public void run() {

    }

    public static void main(String[] args) {

    }
}
