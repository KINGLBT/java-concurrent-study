# volatile 与Java内存模型（JMM）

java-concurrent-chapter01中，已经解释了Java的内存模型，围绕着原子性、可见性和有序性就行展开的。
虽然Java虚拟机和执行系统会对指令进行一定的重排，但是指令重排是有原则的，并非所有的指令都可以随便改变执行位置，volatile就是其中之一，
告诉虚拟机，在这个地方，尤其需要注意，不能随意变动优化目标指令。

## 示例 JDK虚拟机32位中，64位long数据不一致问题解决

public static **volatile** long value = 0l;

定义变量的时候，加上volatile


```java
public class MultiThreadChangeLong implements Runnable {

    public static volatile long value = 0l;

    private long to;

    public MultiThreadChangeLong(long to) {
        this.to = to;
    }

    public void run() {
        while (true) {
            MultiThreadChangeLong.value = to;
            Thread.yield();
        }
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MultiThreadChangeLong(111l));
        Thread t2 = new Thread(new MultiThreadChangeLong(-999l));
        Thread t3 = new Thread(new MultiThreadChangeLong(333l));
        Thread t4 = new Thread(new MultiThreadChangeLong(-444l));
        t1.start();t2.start();t3.start();t4.start();
        // 虚拟机位数
        String arch = System.getProperty("sun.arch.data.model");
        System.out.println(arch+"-bit");
        while (true) {
            System.out.println(MultiThreadChangeLong.value);
        }
    }
}
```