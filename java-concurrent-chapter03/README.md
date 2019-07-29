# 线程创建
线程创建方式有两种，如下：

## 第一种，内部类
```java
public class ThreadInner {

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                System.out.println("Hello,I am t1");
            }
        });
        t1.start();
    }

}
```

## 第二种，传入runnable参数
```java
public class ThreadRunnable implements Runnable {
    public void run() {
        System.out.println("I am runnable");
    }

    public static void main(String[] args) {
        ThreadRunnable runnable = new ThreadRunnable();
        Thread t2 = new Thread(runnable);
        t2.start();
    }
}
```

# 注意：
不要用Thread.run()方法来开启线程，它只会在当前线程中串行执行run()方法的代码。