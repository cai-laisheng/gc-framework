package com.allen.thread.aqs;

import java.util.concurrent.locks.LockSupport;

/**
 * @author xuguocai
 * @date 2022/8/30 16:46
 *
 * thread线程调用LockSupport.park()致使thread阻塞，当mian线程睡眠3秒结束后通过LockSupport.unpark(thread)方法唤醒thread线程,thread线程被唤醒执行后续操作。
 */
public class LockSupportDemo {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "被唤醒");
        });
        thread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(thread);
    }

}
