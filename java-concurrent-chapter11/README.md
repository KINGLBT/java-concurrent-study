# 守护线程（Daemon） 和 线程优先级

## 守护线程（Daemon）

守护线程是一种特殊的线程，是系统的守护者，完成一些系统性的服务，如垃圾回收线程、JIT线程都是守护线程。当一个Java应用内只存在守护线程的时候，
Java虚拟就就会自然退出。

Thread.setDaemon(true)，可以设置线程为守护线程，这个方法必须在start()方法之前，否则守护线程不生效。

### 守护线程使用

```java
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

```

上面的，Main主线程执行完成之后，Java虚拟机就会退出。

执行结果如下:

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter11/11-1.png)


## 线程优先级

Java中的线程可以有自己的优先级。优先级高的线程在竞争资源时会更有优势，更可能抢占资源。

在Java中，使用1-10来表示线程的优先级。JDK中内置的三个优先级数值:

```java
public final static int MIN_PRIORITY = 1;
public final static int NORM_PRIORITY = 5;
public final static int MAX_PRIORITY = 10;
```

数值越大，线程优先级越大。

Thread.setPriority(int priority)方法可以用来设置优先级。

### 线程优先级使用

```java
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
```

## 总结:

+ Thread.setDaemon(true) 将线程设置为守护线程
+ 守护线程必须在start()方法前执行
+ 当Java应用中只剩下守护线程的时候，Java虚拟就就会退出

+ Java中，使用1-10数字表示优先级
+ 数字越大，线程优先级越高
+ 线程优先级高的并非一定比优先级低的线程先获取到资源，只是大部分情况是这样，并不是绝对的
