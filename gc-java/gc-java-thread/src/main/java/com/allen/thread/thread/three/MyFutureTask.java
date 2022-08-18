package com.allen.thread.thread.three;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author xuguocai
 * @date 2022/8/18 17:51
 *
 * https://mp.weixin.qq.com/s/lRttJjY9bHdS4QZaBBE1iw
 *
 * 现在我们应该可以很清楚知道 Thread 、Runnable、FutureTask 和 Callable 的关系：
 *
 * Thread.run() 执行的是 Runnable.run()；
 * FutureTask 继承了 Runnable，并实现了 FutureTask.run()；
 * FutureTask.run() 执行的是 Callable.run()；
 * 依次传递，最后 Thread.run()，其实是执行的 Callable.run()。
 *
 * 所以整个设计方法，其实就是 2 个策略模式，Thread 和 Runnable 是一个策略模式，FutureTask 和 Callable 又是一个策略模式，最后通过 Runnable 和 FutureTask 的继承关系，将这 2 个策略模式组合在一起。
 *
 * FutureTask 继承了 Future，实现对任务的取消、数据获取、任务状态判断等功能。
 *
 * 比如我们经常会调用 get() 方法获取数据，如果任务没有执行完成，会将当前线程放入阻塞队列等待，当任务执行完后，会唤醒阻塞队列中的线程。
 */
public class MyFutureTask extends FutureTask {

    public MyFutureTask(Callable callable) {
        super(callable);
    }

    public static void main(String[] args) {

        MyFutureTask myFutureTask = new MyFutureTask(new MyCallable());
        new Thread(myFutureTask,"线程三").start();
    }
}
