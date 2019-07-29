# 线程终止
一般来说，线程执行完成后，会自动终止，但是，一些后台的线程可能需要常驻系统，他们永远不会执行完成。例如：run方法内是一个循环体。

Thread 中提供了stop()方法，可以立即停止线程。但是，这个方法，不推荐使用，日后可能会从JDK中移除。

Stop方法为什么不推荐使用？

因为过于暴力，有些线程可能执行到一半，也会被强行关闭，就会引起数据不一致问题。

举个例子，如下图

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter4/4-1.png)


ID写完成，正在写入NAME,这个使用，调用stop方法后，写入线程立刻终止，此时，读取线程获取锁，拿到的是写完的ID，未写完的NAME，数据不一致。

## 调用stop方法，终止线程，并模拟数据不一致问题

```java
public class StopThreadUnsaft {

    public static User user = new User();

    public static class ChangeUserRunnable implements Runnable {

        public void run() {
            while (true) {
                synchronized (user) {
                    int v = (int) (System.currentTimeMillis()/1000);
                    user.setUserId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setUserName(v+"");
                }
            }
        }

    }

    public static class ReadObjectThread implements Runnable {

        public void run() {
            while (true) {
                synchronized (user) {
                    if (user.getUserName()!=null && !user.getUserName().equals(user.getUserId().toString())) {
                        System.out.println(user);
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t2 = new Thread(new ReadObjectThread());
        t2.start();

        while(true) {
            Thread t1 = new Thread(new ChangeUserRunnable());
            t1.start();
            Thread.sleep(150);
            t1.stop();
        }
    }


}
```

运行结果如下：

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter4/4-2.png)

可以看出，stop方法，确实暴力，在一些情况下，不管执行没执行完成，立刻停止线程，造成数据不一致。

## 停止线程

可以设置一个标识位，由我们自己决定什么时候，退出线程。

```java
public class StopThreadSaft {

    public static User user = new User();

    public static class ChangeUserThread extends Thread {

        private boolean stopMe = false;

        public void stopMe(){
            stopMe = true;
        }

        public void run() {
            while (true) {
                if (stopMe) {
                    System.out.println("exit by stopMe");
                    break;
                }
                synchronized (user) {
                    int v = (int) (System.currentTimeMillis()/1000);
                    user.setUserId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setUserName(v+"");
                }
            }
        }

    }

    public static class ReadObjectThread extends Thread {

        public void run() {
            while (true) {
                synchronized (user) {
                    if (user.getUserName()!=null && !user.getUserName().equals(user.getUserId().toString())) {
                        System.out.println(user);
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t2 = new Thread(new ReadObjectThread());
        t2.start();

        while(true) {
            Thread t1 = new ChangeUserThread();
            t1.start();
            Thread.sleep(150);
            ((ChangeUserThread) t1).stopMe();
        }
    }


}
```



## 总结
+ Thread.stop()方法可能会造成数据不一致，不推荐使用。

## 思考

上面的停止线程是我们自己手动写的，那么JDK中有没有自带的呢？有，线程中断（interrupt）
