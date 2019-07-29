# Java的内存模型（JMM）

由于并行程序比串行程序复杂得多，并发程序中数据访问的一致性和安全性受到严重挑战。为了保证多个线程间可以高效地，正确地协同工作，需要再定义
一种规则，而JMM也就为此而生。

JMM的关键技术点都是围绕着多线程的原子性，可见性和有序性来建立的

## 原子性（Atomicity）

原子性指一个操作不可中断。即使是多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程干扰。

例如：全局静态变量 int i;两个线程同时对它赋值，线程A给1，线程B给-1，那么i的值要么是1，要么是-1，不会存在其他值。给整形变量赋值的这个操作
就是原子性，不可被打断。

那么有没有不是原子性操作的呢？
在32位系统中，对long 类型变量就行赋值，就是非原子操作的。对于32位操作系统来说，单次次操作能处理的最长长度为32bit，而long类型8字节64bit，
所以对long的读写都要两条指令才能完成（即每次读写64bit中的32bit）。

### long 原子性检测
```java
public class MultiThreadChangeLong implements Runnable {

    public static long value = 0l;

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
            if (MultiThreadChangeLong.value != 111l && MultiThreadChangeLong.value != -999l
            && MultiThreadChangeLong.value != 333l && MultiThreadChangeLong.value != -444l) {
                System.out.println(MultiThreadChangeLong.value);
            }
        }
    }
}
``` 
+ 1、在64位JVM中，使用多线程对long型变量赋值，并读取查看是否正确

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter1/1-4.png)

+ 2、在32位JVM中，使用多线程对long型变量赋值，并读取查看是否正确

![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter1/1-3.png)


## 可见性（Visibility）

可见性指当一个线程修改了某一个共享变量的值得时候，其他线程是否能够立即知道这个修改。这个问题存在于并行程序中，其他线程修改了未必可以马上知道。
造成这种结果的主要原因是：
+ 1.编译器的优化或者硬件优化
例如，在CPU1和CPU2各运行了一个线程，他们共享变量t,由于编译器优化或者硬件优化的缘故，在CPU1上的线程将变量t进行了优化，
将其缓存在cache中或者寄存器中。这种情况下，CPU2上的某个线程修改了变量t的实际值，那么CPU1上的线程可能无法意识到值已经
改动。依然从缓存中或者寄存器中读取。由此，就产生了可见性问题。
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter1/1-5.png)

+ 2.指令重拍
编译器做了一些优化，例如指令重拍，这种造成指令执行的顺序不一致，可能会引起可见性问题。
例如：
线程1 执行以下两条指令  1: r2=A; 2:B = 1
线程2 执行以下两条指令  3: r1=B; 4:A = 2

从指令的执行顺序上来看，r2 == 2 并且 r1 == 1 似乎是不可能出现。如果r2 == 2,则指令4要优于指令1先执行，指令3肯定在指令4之前执行。
指令的执行书序 需要时3-->4-->1-->2,这种情况下，才能保证r2 == 2,此时r1肯定不是1，所以，r2 == 2 并且 r1 == 1几乎不可能出现。

但实际情况，并没有办法从理论上保证这种情况不出现，因为编译器可能将指令重排成：
线程1 执行以下两条指令  1: B=1; 2:r2 = A
线程2 执行以下两条指令  3: r1=B; 4:A = 2
这种情况下 r2 == 2,则指令2的执行一定需要在指令4之后，如果r1 == 1,则指令3的执行顺序一定要在指令1之后，
因此，指令执行顺序 1-->3-->4-->2,这种情况下，会出现r2 == 2 并且 r1 == 1

这个例子就说明，在一个线程中观察另一个线程的变量，他们的值是否能观测到，何时能观测到，没有保证的。


## 有序性（Ordering）
有序性的原因是程序在执行时，可能会进行指令重排，重排后的指令与原指令的顺序未必一直。

# 哪些指令不能重排:Happen-Before规则

虽然Java虚拟机和执行系统会对指令进行一定的重排，但是指令重排是有原则的，并非所有的指令都可以随便改变执行位置，如下原则
是指令重排不可违的：

+ 程序顺序原则: 一个线程内保证语义的串行性
+ volatile规则: volatile变量的写先于读发生，这保证了volatile变量的可见性。
+ 锁规则: 解锁（unlock）必然发生于随后的加锁（lock）前
+ 传递性: A先于B,B先于C，那么A必然先于C
+ 线程的start()方法优先于它的每一个动作
+ 线程的所有操作优先于线程的终结（Thread.join()）
+ 线程的中断（interrupt()）先于被中断线程的代码
+ 对象的构造函数执行，结束先于finalize()方法