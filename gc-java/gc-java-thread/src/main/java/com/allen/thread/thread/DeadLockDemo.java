package com.allen.thread.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @program: MultiThread
 * @description: DeadLockDemo
 * @author: allen小哥
 * @Date: 2019-11-26 15:40
 *
 * 测试过程:
 *  先找到线程号：jps -l
 *  根据线程号查找线程执行过程:jstack -l 83624
 *
 **/
public class DeadLockDemo {

    private static String resourceA = "A";
    private static String resourceB ="B";

    public static void deadLock(){
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                synchronized (resourceA){
                    System.out.println("获取 resourceA 锁:"+Thread.currentThread().getName());
                    try {
                        Thread.sleep(2000);
                        synchronized (resourceB){
                            System.out.println("获取 resourceB 锁："+Thread.currentThread().getName());
                        }
                    }catch (Exception e){
                        System.out.println("抛出异常,{}"+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            public void run() {
                synchronized (resourceB){
                    System.out.println("获取 resourceA 锁:"+Thread.currentThread().getName());
                    synchronized (resourceA){
                        System.out.println("获取 resourceB 锁:"+Thread.currentThread().getName());
                    }
                }
            }
        });
        threadA.start();
        threadB.start();
    }

    public static void main(String[] args) {
        findDeadlockedThreads();
        deadLock();
    }

    /**
     * 如果我们是开发自己的管理工具，需要用更加程序化的方式扫描服务进程、定位死锁，
     * 可以考虑使用 Java 提供的标准管理 API，ThreadMXBean，其直接就提供了 findDeadlockedThreads() 方法用于定位
     */
    public static void findDeadlockedThreads(){
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        Runnable dlCheck = new Runnable() {

            @Override
            public void run() {
                long[] threadIds = mbean.findDeadlockedThreads();
                if (threadIds != null) {
                    ThreadInfo[] threadInfos = mbean.getThreadInfo(threadIds);
                    System.out.println("Detected deadlock threads:");
                    for (ThreadInfo threadInfo : threadInfos) {
                        System.out.println(threadInfo.getThreadName());
                    }
                }
            }
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // 稍等5秒，然后每10秒进行一次死锁扫描
        scheduler.scheduleAtFixedRate(dlCheck, 5L, 10L, TimeUnit.SECONDS);
    }
}
