# 线程中断

在Java中，线程中断是一种重要的线程协作机制。在java-concurrent-chapter04中，知道stop方法可以立刻终止线程，但是
这种方式会导致数据不一致问题。基于这个问题，自定义了一个stopMe方法，完善线程终止，造成数据不一致问题。那么，在JDK中，
是否有更强大的支持呢？有，就是线程中断。

严格意义上讲，线程中断不会马上使线程退出，而是给线程发送了一个通知，至于目标线程接到通知后如何处理，则完全由目标线程去
决定。如果中断后，立刻退出，和stop()就没有任何区别了，一样会碰到数据不一致问题。

线程中断的三个重要方法：

```java
public void interrupt()  // 中断线程
public boolean isInterrupted() // 判断是否中断
public static boolean interrupted() // 判断是否中断，并清除当前中断状态
```

interrupt 通知目标线程中断，也就是设置中断标志位。中断标志位标识当前线程已经中断了。isnterrupted判断当前线程是否被中断(
通过检查中断标识)。最后的静态方法interrupted也可以用来判断是否中断，但同时会清除中断标志位状态。

## 示例1
```java
public class Test {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("线程没有中断？");
                }
            }
        });

        t1.start();
        t1.interrupt();
    }

}

```

结果：线程没有停止，一直在执行

Thread.interrupt()只是设置了中断标志位，在t1中并没有中断处理逻辑。如果希望t1正常终止执行，需要设置中断逻辑：

## 示例2 完整的线程中断
```java
public class TestInterrupt {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while (true) {

                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("线程中断了");
                        break;
                    }
                    System.out.println("线程没有中断？");
                }
            }
        });

        t1.start();
        t1.interrupt();
    }

}
```

## 线程中断和前面的stopMe()区别

从上面可以看出来，线程中断和stopMe类似，都是设置中断标志位，然后，处理中断逻辑。但是线程中断更全面。例如，在线程体中出现类似于wait()方法
或者sleep()方法这样的操作，则只能通过中断来标识了。

## 线程中断在Thread.sleep()中应用

```java
public static native void sleep(long millis) throws InterruptedException;
```
Thread.sleep()方法会让当前线程休息若干毫秒，如果线程处于sleep()时，如果中断，这个异常聚会捕获。

### 线程不在休眠状态下，执行中断，sleep不会捕获异常
```java
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
                        //Thread.currentThread().interrupt();
                    }

                    System.out.println("我怎么还么有中断?");
                }
            }
        });
        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
    }

}

```

结果：

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter5/5-2.png)

### 线程在休眠状态下，执行中断，需要再次执行中断方法，否则线程不终止

```java
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
                        //Thread.currentThread().interrupt();
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

```

结果：
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter5/5-3.png)

### Thread.sleep()或Object.wait()会捕获中断异常后，中断逻辑也处理了，为什么线程还不中断?

查看Thread.sleep()源码，如下图：
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter5/5-4.png)

从源码可以分析出，Thread处理中断异常后，会将中断标志位清除，此时，线程是未中断，因此，在异常处理中，需再次设置中断标志位
```java
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

```

结果：
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter5/5-5.png)




## 总结

+ 线程调用interrupt()方法之后，只会将标志位设置为线程中断，如果不做中断逻辑处理，线程还是不会中断

+ Thread.sleep()或Object.wait()会捕获中断异常后，需要再次执行interrupt中断自己，因为Thread.sleep()
和Object.wait()方法会清除中断标志位

+ Thread.sleep()不会释放任何资源









