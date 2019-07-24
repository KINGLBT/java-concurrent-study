# Java并行程序基础

## 有关线程必须知道是事

进程（Process）是计算机中的程序关于某数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，是操作系统结构的基础。
在早期面向进程设计的计算机结构中，进程是程序的基本执行实体；在当代面向线程设计的计算机结构中，进程是线程的容器。
程序是指令、数据及其组织形式的描述，进程是程序的实体。

线程就是轻量级进程，是程序执行的最小单位。使用多线程去进行并发设计，是因为线程间的切换和调度成本远远小于进程。

线程的生命周期，如下图：
![Image text](https://raw.githubusercontent.com/KINGLBT/java-concurrent-study/master/image/chapter1/1-1.png)


```java
public enum State {
    NEW,
    RUNNABLE,
    BLOCKED,
    WAITING,
    TIMED_WAITING,
    TERMINATED;
} 
```