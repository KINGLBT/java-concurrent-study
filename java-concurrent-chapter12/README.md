# 关键字synchronized

线程使用关注的重点是线程安全，一般来说，使用多线程是为了提高执行效率，但前提是，高效率不能牺牲真确性。因此，线程安全是并行程序的根基。
在前面的示例中，long在32位系统中，会造成数据不一致，使用volidate关键字可以保证原子性，可见性等，但是并不能真正保证线程安全。它只能
确保一个线程修改后，其他线程能够看到这个改动。但当两个线程同时修改某一个数据时候，依然会产生冲突。

## 示例 多线程同时修改volidate定义的变量

下面的程序，输出的i是多少？

```java
public class VolidateThread {

    public static volatile  int i = 0;

    public static class VolidateRunnable implements Runnable {

        public void run() {
            for (int j = 0; j < 100000000; j++) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolidateRunnable runnable = new VolidateRunnable();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}

```

结果分析：
发现i的值，基本上全部<200000000;因为两个线程同时对i进行写入的时候，一个线程的值会覆盖另一个线程的值。虽然i被volatile声明。

要从根本上解决这种问题，我们必须保证多个线程对i的操作同步，也就是同一时刻，只能有一个线程操作变量i。Java中提供了一个重要的关键字synchronized来
实现这个功能。

## 关键字synchronized

关键字synchronized的作用是实现线程同步，同一时刻，只会有一个线程进入同步块，从而保证线程间的安全性。

关键字synchronized的三种用法：

+ 指定加锁对象：对给定对象加锁，进入同步代码前需要获取对象的锁
+ 直接作用于实例方法：相当于当前实例加锁，进入同步代码前需要获得当前实例的锁
+ 直接作用于静态方法：相当于当前类加锁，进入同步代码前需要获取当前类的锁

### 关键字synchronized 指定加锁对象

指定加锁对象：**synchronized (runnable)**

```java
public class SynchronizedOne {

    public static SynchronizedOneRunnable runnable = new SynchronizedOneRunnable();

    public static volatile  int i = 0;

    public static class SynchronizedOneRunnable implements Runnable {

        public void run() {
            for (int j = 0; j < 10000000; j++) {
                synchronized (runnable) {
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}
```

### 关键字synchronized 作用于实例方法


作用于实例的方法上 **public synchronized void increase()**

```java
public class SynchronizedTwo implements Runnable{

    public static int i = 0;

    public synchronized void increase() {
        i++;
    }

    public void run() {
        for (int j = 0; j < 10000000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTwo runnable = new SynchronizedTwo();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}
```

思考：

将上述代码修改成如下，是什么情况？

```java
Thread t1 = new Thread(new SynchronizedTwo());
Thread t2 = new Thread(new SynchronizedTwo());
```
在实例的方法上加同步块，先需要获取当前实例作为锁，这个时候，每次都 new SynchronizedTwo()，实例不是同一个实例，因此，线程t1和t2能同时获取锁，
也就是可以同时修改临界资源i,并没有实现线程同步。

### 关键字synchronized 作用于静态方法

作用于静态方法上 **public static synchronized void increase()**

```java
public class SynchronizedThree implements Runnable{

    public static int i = 0;

    public static synchronized void increase() {
        i++;
    }

    public void run() {
        for (int j = 0; j < 10000000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new SynchronizedThree());
        Thread t2 = new Thread(new SynchronizedThree());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}

```