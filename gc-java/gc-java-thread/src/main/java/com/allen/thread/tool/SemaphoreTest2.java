package com.allen.thread.tool;

import java.util.concurrent.Semaphore;

/**
 * @author xuguocai on 2021/7/19 16:56
 */
public class SemaphoreTest2 {

    public static void main(String[] args) throws Exception{
        //new Semaphore(permits)：初始化许可证数量的构造函数
        Semaphore semaphore = new Semaphore(5);

        //new Semaphore(permits,fair):初始化许可证数量和是否公平模式的构造函数
        semaphore = new Semaphore(5, true);

        //isFair()：是否公平模式FIFO
        System.out.println("是否公平FIFO：" + semaphore.isFair());

        //availablePermits():获取当前可用的许可证数量
        System.out.println("获取当前可用的许可证数量：开始---" + semaphore.availablePermits());

        //acquire():获取1个许可证
        //---此线程会一直阻塞，直到获取这个许可证，或者被中断(抛出InterruptedException异常)。
        semaphore.acquire();
        System.out.println("获取当前可用的许可证数量：acquire 1 个---" + semaphore.availablePermits());

        //release()：释放1个许可证
        semaphore.release();
        System.out.println("获取当前可用的许可证数量：release 1 个---" + semaphore.availablePermits());

        //acquire(permits):获取n个许可证
        //---此线程会一直阻塞，直到获取全部n个许可证,或者被中断(抛出InterruptedException异常)。
        semaphore.acquire(2);
        System.out.println("获取当前可用的许可证数量：acquire 2 个---" + semaphore.availablePermits());

        //release(permits):释放n个许可证
        semaphore.release(2);
        System.out.println("获取当前可用的许可证数量：release 1 个---" + semaphore.availablePermits());

        //hasQueuedThreads():是否有正在等待许可证的线程
        System.out.println("是否有正在等待许可证的线程：" + semaphore.hasQueuedThreads());

        //getQueueLength():正在等待许可证的队列长度(线程数量)
        System.out.println("正在等待许可证的队列长度(线程数量)：" + semaphore.getQueueLength());

        Thread.sleep(10);
        System.out.println();
        //定义final的信号量
        Semaphore finalSemaphore = semaphore;
        new Thread(() -> {
            //drainPermits():获取剩余的所有的许可证
            int permits = finalSemaphore.drainPermits();
            System.out.println(Thread.currentThread().getName() + "获取了剩余的全部" + permits + "个许可证.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //释放所有的许可证
            finalSemaphore.release(permits);
            System.out.println(Thread.currentThread().getName() + "释放了" + permits + "个许可证.");
        }).start();

        Thread.sleep(10);
        new Thread(() -> {
            try {
                //有一个线程正在等待获取1个许可证
                finalSemaphore.acquire();
                System.out.println(Thread.currentThread().getName() + "获取了1个许可证.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //释放1个许可证
            finalSemaphore.release();
            System.out.println(Thread.currentThread().getName() + "释放了1个许可证.");

        }).start();
        Thread.sleep(10);
        System.out.println();
        System.out.println("获取当前可用的许可证数量：drain 剩余的---" + finalSemaphore.availablePermits());
        System.out.println("是否有正在等待许可证的线程：" + finalSemaphore.hasQueuedThreads());
        System.out.println("正在等待许可证的队列长度(线程数量)：" + finalSemaphore.getQueueLength());
        System.out.println();

        Thread.sleep(10);
        new Thread(() -> {
            try {
                //有一个线程正在等待获取2个许可证
                finalSemaphore.acquire(2);
                System.out.println(Thread.currentThread().getName() + "获取了2个许可证.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //释放两个许可证
            finalSemaphore.release(2);
            System.out.println(Thread.currentThread().getName() + "释放了2个许可证.");
        }).start();
        Thread.sleep(10);
        System.out.println();
        System.out.println("获取当前可用的许可证数量：drain 剩余的---" + finalSemaphore.availablePermits());
        System.out.println("是否有正在等待许可证的线程：" + finalSemaphore.hasQueuedThreads());
        System.out.println("正在等待许可证的队列长度(线程数量)：" + finalSemaphore.getQueueLength());
        System.out.println();

        Thread.sleep(5000);
        System.out.println();
        System.out.println("获取当前可用的许可证数量：---" + finalSemaphore.availablePermits());
        System.out.println("是否有正在等待许可证的线程：" + finalSemaphore.hasQueuedThreads());
        System.out.println("正在等待许可证的队列长度(线程数量)：" + finalSemaphore.getQueueLength());
    }

}
