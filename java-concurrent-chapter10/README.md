# 线程组

在一个系统中，如果线程的数量很多，而且功能分配比较明确，就可以将相同功能的线程防止在同一个线程组里。想要轻松处理十几个甚至上百个线程，最好的
办法还是通过线程组来操作。

## 线程组创建以及使用

```java
public class ThreadGroupStudy {

    public static class PrintThreadRunnable implements Runnable {

        public void run() {
            String gropAndName = Thread.currentThread().getThreadGroup().getName()+"-"+Thread.currentThread().getName();
            while (true) {
                System.out.println("I am "+gropAndName);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        // 创建线程组
        ThreadGroup group = new ThreadGroup("PrintThreadGroup");
        // 创建线程，并指定线程组以及线程名称
        Thread t1 = new Thread(group,new PrintThreadRunnable(),"T1");
        Thread t2 = new Thread(group,new PrintThreadRunnable(),"T2");
        t1.start();
        t2.start();
    }

}
```

## 线程组中比较常用的两个方法

+ 线程组中活跃的线程数

group.activeCount()

+ 线程组中的线程列表

group.list();


线程组还可以执行stop()等方法，统一停用所有的线程，和线程的stop()一样，不推荐使用，会发生数据不一致问题。
