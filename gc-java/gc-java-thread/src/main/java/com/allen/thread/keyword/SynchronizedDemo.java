package com.allen.thread.keyword;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuguocai
 * @date 2022/8/29 09:43
 */
public class SynchronizedDemo implements Runnable{
    private static int count = 0;
    private static AtomicInteger count1 = new AtomicInteger(0);

    @Override
    public void run() {
        // 类对象
        synchronized (SynchronizedDemo.class){
            for (int i =0; i <2000;i++){
                count++;
                count1.incrementAndGet();
            }
        }

    }

    public static void methodClass() {
        // 类对象
        synchronized (SynchronizedDemo.class){
            for (int i =0; i <2000;i++){
                count++;
                count1.incrementAndGet();
            }
        }

    }

    public void method() {
        // 该类的对象
        synchronized (this) {
            System.out.println("synchronized 5555");
        }
    }

    public static void main(String[] args) {
//        method2();
        //methodClass\method1 锁住的是类对象，并且还有一个同步静态方法，锁住的依然是该类的类对象
        methodClass();
        method1();
    }

    private static void method1() {
    }
    // 测试变量的安全性
    public static void method2(){
        for (int i = 0;i< 10;i++){
            Thread thread= new Thread(new SynchronizedDemo());
            thread.start();
        }

        try {
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("result="+count);
        System.out.println("result="+count1.get());
    }
}
