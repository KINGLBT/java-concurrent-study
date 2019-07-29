package com.rebote.concurrent;

/**
 * @Auther: Da Shuai
 * @Date: 2019/7/27 13:32
 * @Description:
 * @Company: zytech
 * @Email: 1043489207@qq.com
 */
public class MultiThreadChangeLong implements Runnable {

    public static long value = 0l;

    private long to;

    public MultiThreadChangeLong(long to) {
        this.to = to;
    }

    public void run() {
        while (true) {
            MultiThreadChangeLong.value = to;
            Thread.yield();
        }
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MultiThreadChangeLong(111l));
        Thread t2 = new Thread(new MultiThreadChangeLong(-999l));
        Thread t3 = new Thread(new MultiThreadChangeLong(333l));
        Thread t4 = new Thread(new MultiThreadChangeLong(-444l));
        t1.start();t2.start();t3.start();t4.start();
        // 虚拟机位数
        String arch = System.getProperty("sun.arch.data.model");
        System.out.println(arch+"-bit");
        while (true) {
            if (MultiThreadChangeLong.value != 111l && MultiThreadChangeLong.value != -999l
            && MultiThreadChangeLong.value != 333l && MultiThreadChangeLong.value != -444l) {
                System.out.println(MultiThreadChangeLong.value);
            }
        }
    }
}
