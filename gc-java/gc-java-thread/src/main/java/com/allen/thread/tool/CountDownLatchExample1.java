package com.allen.thread.tool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xuguocai on 2021/2/3 15:17
 */
public class CountDownLatchExample1 {
    // 请求的数量
    private static final int threadCount = 20;

    public static void main(String[] args) throws InterruptedException {
//        demo1();
        demo2();
    }

    private static CountDownLatch startSignal = new CountDownLatch(1);
    //用来表示裁判员需要维护的是6个运动员
    private static CountDownLatch endSignal = new CountDownLatch(6);
    private static void demo2() throws InterruptedException{
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 6; i++) {
            executorService.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 运动员等待裁判员响哨！！！");
                    startSignal.await();
                    System.out.println(Thread.currentThread().getName() + "正在全力冲刺");
                    endSignal.countDown();
                    System.out.println(Thread.currentThread().getName() + "  到达终点");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("裁判员发号施令啦！！！");
        startSignal.countDown();
        endSignal.await();
        System.out.println("所有运动员到达终点，比赛结束！");
        executorService.shutdown();
    }

    private static void demo1() throws InterruptedException {
        // 创建一个具有固定线程数量的线程池对象（如果这里线程池的线程数量给太少的话你会发现执行的很慢）
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        System.out.println("执行 CountDownLatch 操作，主线程 mian 开始");
        for (int i = 0; i < threadCount; i++) {
            final int threadnum = i;
            threadPool.execute(() -> {// Lambda 表达式的运用
                try {
                    test(threadnum);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    //count 的值等于 0，然后主线程就能通过 await()方法，恢复执行自己的任务。
                    countDownLatch.countDown();// 表示一个请求已经被完成，可以通知
                    System.out.println("  线程名:"+Thread.currentThread().getName()+", CountDownLatch 剩下多少："+countDownLatch.getCount());
                }

            });
        }
        //这样主线程的操作就会在这个方法上阻塞，直到其他线程完成各自的任务。
        countDownLatch.await(); // 进入等待
        threadPool.shutdown();
        System.out.println("主线程 mian 执行 finish");
    }

    public static void test(int threadnum) throws InterruptedException {
        Thread.sleep(1000);// 模拟请求的耗时操作
        System.out.println("CountDownLatch 的执行方法 threadnum:" + threadnum+"  线程名:"+Thread.currentThread().getName());
        Thread.sleep(1000);// 模拟请求的耗时操作
    }
}
