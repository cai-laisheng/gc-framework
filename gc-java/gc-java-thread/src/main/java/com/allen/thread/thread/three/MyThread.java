package com.allen.thread.thread.three;

/**
 * @author xuguocai
 * @date 2022/8/18 17:24
 *
 * 线程包含 4 个状态：创建 -> 就绪 -> 运行 -> 结束。
 *
 * 当执行 start() 后，线程进入就绪状态，当对应的线程抢占到 cpu 调度资源之后，进入运行状态，此时调用的是 run 方法，执行完毕之后就结束了。
 */
public class MyThread extends Thread{
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + "已经运行");
    }

    public static void main(String[] args) {
        new MyThread("线程一").start();
    }
}
