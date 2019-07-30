# 等待线程结束（join）和谦让（yeild）

## 等待线程结束（join）
一个线程的输入，有时候非常依赖另一个或者多个线程的输出，此时，这个线程就需要等待依赖线程执行完毕。JDK提供了
join()操作来实现这个功能。

```java
public final void join() throws InterruptedException
public final synchronized void join(long millis)
```

第一个表示无限等待，会一直阻塞当前线程，知道目标线程执行完毕。
第二个给出了一个最大等待时间，如果超过给定时间，目标线程还在执行，当前线程将不再等待，继续往下执行。

### 线程等待示例
```java
public class ThreadJoin {

    public static int i = 0;

    public static class AddThread extends Thread {

        @Override
        public void run() {
            for (int j = 0; j < 10000; j++) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new AddThread();
        t1.start();
        t1.join();
        System.out.println(i);
    }

}
```

查看运行结果：
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter8/8-1.png)

如果不加t1.join()的话，值可能是0，或者其他

## join的底层

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter8/8-1.png)

如图所示，t1.join()，如果不设置等待时间，那么执行的代码是：

```java
while (isAlive()) {
    wait(0);
}
```

isAlive()会检测t1线程的状态，如果是活动状态，则当前线程会进行wait()操作。
在java-concurrent-chapter06中，知道wait()中的线程，只能被notify或者notifyAll唤醒，那么当前线程什么时候会被唤醒呢？
被等待的线程会在退出前执行notifyAll()方法通知所有的等待线程，继续执行。

大致流程如下：
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter8/8-3.png)

因此，需要注意的是，不要在Thread对象上使用类似wait()方法或者notify()方法，因为这很可能会影响系统API的工作，或者被系统API影响。

### 验证在Thread对象上使用类似wait()方法或者notify()方法，可能影响系统API的工作

```java
public class ThreadUseWait {

    public static Thread t1 = new Thread1();

    public static class Thread1 extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Thread2 extends Thread {

        @Override
        public void run() {
            synchronized (t1) {
                System.out.println("T2获取到t1监听器");
                try {
                    System.out.println("T2线程进行等待，释放资源");
                    t1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T2被唤醒了，继续执行");
            }
        }
    }


    public static class Thread3 extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (t1) {
                System.out.println("T3获取到t1监听器");
                System.out.println("T3线程进行线程唤醒，释放资源");
                t1.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t2 = new Thread2();
        Thread t3 = new Thread3();
        t1.start();
        t2.start();
        t3.start();
        t1.join();
    }
}
```
运行结果如下:
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter8/8-4.png)

从运行结果可以看出来，t3还没有执行notify通知，t2就已经被唤醒了。得出：
**不要在Thread对象上使用类似wait()方法或者notify()方法，因为这很可能会影响系统API的工作，或者被系统API影响**

## 线程谦让（yield）

```java
public static native void yield();
```

这是一个静态方法，一旦执行，它会使当前线程让出CPU。让出CPU不代表不执行了，还会进行CPU资源的争夺


## 总结
+ **如果一个线程依赖另一个或者多个线程，可以使用线程等待join**
+ **不要在Thread对象上使用类似wait()方法或者notify()方法，因为这很可能会影响系统API的工作，或者被系统API影响。**
+ **yield 会使当前线程让出CPU，还是会进行CPU资源的争夺**






