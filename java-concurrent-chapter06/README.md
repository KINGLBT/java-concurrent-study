# 线程等待（wait）和通知（notify）

线程等待和线程通知，是多线程间协作比较常见的操作。通过JDK中的wait()和notify()方法来实现。这两个方法并不是在Thread类中，
而是在Object类中。
```java
public final void wait() throws InterruptedException;
public final native void notify();
```

## 等待（wait）和通知（notify）工作方式

如果一个线程调用了object.wait()方法后，那么该线程会进入object对象的等待队列。这个等待队列中，可能会有多个线程，因为系统运行多个
线程同时等待某一个对象。
当object.notify()方法被调用时，它就会从等待队列中随机选择一个线程，并将其唤醒。因此，**唤醒是不公平的**，是随机选择的，并不是先等待
就会被先选中。
除了notify方法外，Object对象还有一个类似的notifyAll方法，通知等待队列中的全部线程，将他们全部唤醒。

如下工作过程:

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter6/6-1.png)

## 案例
```java
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
```


## 等待（wait）和睡眠（sleep）区别

+ 与sleep相比较，wait()可以被notify唤醒，sleep无法被唤醒

+ 与sleep相比较，wait()方法会释放目标对象的锁，而Thread.sleep()不会释放任何资源。

# 总结

+ wait()和notify() 是Object中的方法，所有对象都可以wait()和notify()

+ Object.wait()对象不能随便调用，必须包含在synchronized代码块中

+ 调用wait()或者notify()方法的时候，必须获得目标对象的一个监视器，调用完成后，会释放监视器

+ 与sleep相比较，wait()可以被notify唤醒，sleep无法被唤醒

+ 与sleep相比较，wait()方法会释放目标对象的锁，而Thread.sleep()不会释放任何资源。
