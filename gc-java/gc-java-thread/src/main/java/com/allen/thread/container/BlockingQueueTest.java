package com.allen.thread.container;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @program: MultiThread
 * @description: BlockingQueueTest
 * @author: allen小哥
 * @Date: 2019-12-14 19:46
 **/
@Slf4j
public class BlockingQueueTest {

    public static final LinkedBlockingDeque LINKED_BLOCKING_DEQUE = new LinkedBlockingDeque();

    public static void main(String[] args) throws Exception{
        // 数组
//        arrayBlockingQueue();
        // Delayed接口的数据的无界阻塞队列
//        delayQueue();

        linkedBlockingQueue();
    }

    public static void synchronousQueue(){
        //每个插入操作必须等待另一个线程进行相应的删除操作
        SynchronousQueue synchronousQueue = new SynchronousQueue();

    }

    public static void priorityBlockingQueue(){
        //支持优先级的无界阻塞队列
        PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue();

    }

    public static void linkedTransferQueue(){
        //链表数据结构构成的无界阻塞队列
        LinkedTransferQueue linkedTransferQueue = new LinkedTransferQueue();

    }

    public static void linkedBlockingQueue() throws InterruptedException{
        //链表实现的有界阻塞队列
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        for (long i=1;i<10;i++){
            // put方法，如果没有空间，会阻塞一直等到有空间
            linkedBlockingQueue.put(i);
        }

        while (true){
            Object take = linkedBlockingQueue.take();
            System.out.println("linkedBlockingQueue:"+take);
        }
    }

    public static void linkedBlockingDeque(){
        //基于链表数据结构的有界阻塞双端队列
        LinkedBlockingDeque linkedBlockingDeque = new LinkedBlockingDeque();

    }

    public static void delayQueue() throws InterruptedException{
        //是一个存放实现Delayed接口的数据的无界阻塞队列
        DelayQueue<DelayEntity> delayQueue = new DelayQueue();
        for (long i=1;i<10;i++){
            // put方法，如果没有空间，会阻塞一直等到有空间
            delayQueue.put(new DelayEntity(String.valueOf(i),i));
        }

        while (true){
            // ake，获取不到就阻塞
            DelayEntity take = delayQueue.take();
            System.out.println("参数:" + take.getStr() + ";计划执行时间:" + take.showScheduleTime() + ";实际执行时间:" + new Date());
        }
    }

    public static void arrayBlockingQueue(){
        //数组实现的有界阻塞队列
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(5);
        blockingQueue.add("早上好");
        boolean offer = blockingQueue.offer("中午好");
        log.info("offer==="+offer);
        Object peek = blockingQueue.peek();
        log.info("peek="+peek);

        blockingQueue.remove();

        Object poll = blockingQueue.poll();
        log.info("poll="+poll);
    }
}
