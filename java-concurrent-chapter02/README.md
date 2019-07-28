# 重入锁

同步控制是并发程序必不可少的重要手段。synchronized就是一种简单的控制方法，决定了一个线程是否可以访问临界资区资源。同时，Object.wait()方法和Object.notify()方法
起到了线程等待和通知的作用。这些工具对于实现复杂的多线程写作起到了重要作用。

重入锁就是synchronized、Object.wait()、Object.notify()的替代品。

## 关键字synchronized的功能扩展：重入锁

重入锁能够完全替代synchronized，JDK5.0之前，重入锁性能远远优于synchronized，JDK6.0之后，两者新能差距不大。

重入锁使用java.util.concurrent.locks.ReentrantLock类来实现。

### 重入锁简单示例

```java
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
``` 

与synchronized相比，需要指定何时加锁，何时释放。重入锁对逻辑控制的灵活性要比synchronized好。

重入锁的另一个特性，允许一个线程连续两次获得同一把锁，且不会产生死锁，但是如果同一个线程多次获得锁，
那么在释放锁的时候，也必须释放相同次数。

### 同一个线程多次获得同一把锁

 


### 注意：
+ 退出临界区的时候，必须记得释放锁，否则，其他线程永远无法访问临界区了。







   

