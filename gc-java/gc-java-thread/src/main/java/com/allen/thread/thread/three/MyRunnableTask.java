package com.allen.thread.thread.three;

/**
 * @author xuguocai
 * @date 2022/8/18 17:29
 *
 * 这里 MyRunnableTask 就是一个 Runnable，实现了 run() 方法，作为 Thread() 的入参。
 *
 * Runnable 和 Thread 的关系：
 *
 * MyRunnableTask 继承 Runnable，并实现了 run() 方法；
 * Thread 初始化，将 MyRunnableTask 作为自己的成员变量；
 * Thread 执行 run() 方法，线程处于“就绪”状态；
 * 等待 CPU 调度，执行 Thread 的 run() 方法，但是 run() 的内部实现，其实是执行的 MyRunnableTask.run() 方法，线程处于“运行”状态。
 *
 * 源码执行过程：
 * 1、在 Thread 初始化时，MyRunnable 作为入参 target，最后赋值给 Thread.target：
 * private Thread(ThreadGroup g, Runnable target, String name,ong stackSize, AccessControlContext acc,boolean inheritThreadLocals){}
 *
 * 2、当执行 Thread.run() 时，其实是执行的 target.run()，即 MyRunnable.run()，这个是典型的策略模式：
 */
public class MyRunnableTask implements Runnable{

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + "已经运行");
    }

    public static void main(String[] args) {
        new Thread(new MyRunnableTask(),"线程二").start();
    }
}
