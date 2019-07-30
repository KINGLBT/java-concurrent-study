# 线程挂起（suspend）和继续执行（resume）线程

线程挂起和继续执行，是一对相反的操作。被挂起的线程，必须等到resume()方法操作后，才能继续执行。

如下图所示，这两个方法已经被标记为废弃方法。不推荐使用。
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter7/7-1.png)

## 不推荐使用suspend（）方法

suspend()方法，线程挂起的时候，并不会释放任何资源。意外情况下，挂起的线程没有被操作继续执行，就会导致资源一直被占用，
其他挂起的线程很难有机会继续执行。
被挂起的线程状态还是Runnable,也影响对线程状态的判断

如下图，这种情况下，resume意外发生于suspend之前，造成资源一直被占用，其他线程无法获取锁。

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter7/7-2.png)


java-concurrent-chapter06中，notify意外发生于wait()之前，是否也会造成这种问题？
不会，wait()方法调用后，会自动释放占用的资源，其他线程可以获取锁。不会造成整体系统工作不正常

## 示例 模拟resume意外发生于suspend之前，并且查看线程挂起的线程状态

```java
public class BadSuspend {

    public static Object u = new Object();

    public static class ChangeObjectThread extends Thread {

        public ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in "+getName());
                Thread.currentThread().suspend();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new ChangeObjectThread("t1");
        Thread t2 = new ChangeObjectThread("t2");
        t1.start();
        t2.start();
        t1.resume();
        t2.resume();
        t1.join();
        t2.join();
    }


}

```

运行结果如下:

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter7/7-3.png)

从图中可以看出来，线程2没有打印出 in t2,说明，线程2没有获取到资源锁。
但是从代码中，可以看出来t1.resume()方法已经执行了，t1线程所占用的资源锁，已经被释放了。理论上t2能获取锁，并且打印出结果。
造成这种结果原因就是resume意外发生于suspend之前，线程t1挂起不释放资源，其他线程无法使用临界资源。

查看java线程状态:
+ jps查看进程PID，执行jps命令，如下图
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter7/7-4.png)

+ jstack查看进程中的线程状态，jstack -l pid，如下图

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter7/7-5.png)


从上图可以看出来，线程t2在等待锁，线程t1的状态为RUNNABLE状态，并没有执行完成。并从17行代码出，看出线程是挂起的
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter7/7-6.png)


## 示例 线程挂起，继续执行，不影响其他线程

```java
public class GoodSuspend {

    public static Object u = new Object();

    public static class ChangeObjectThread extends Thread {

        public ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in "+getName());
                Thread.currentThread().suspend();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new ChangeObjectThread("t1");
        Thread t2 = new ChangeObjectThread("t2");
        t1.start();
        Thread.sleep(2000);
        t2.start();
        t1.resume();
        Thread.sleep(2000);
        t2.resume();
        t1.join();
        t2.join();
    }

}

```

运行结果：
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter7/7-7.png)

可以看出线程执行完成，程序自动退出



## 总结:

+ suspend()不推荐使用，线程挂起，并不是放资源。
+ 线程执行suspend()挂起后，线程的状态依旧为RUNNABLE状态。
+ 如果意外情况下，suspend挂起后，会造成其他线程阻塞，无法进行。





