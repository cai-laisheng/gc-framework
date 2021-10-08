package com.allen.algorithm.queue;

/**
 * @Author Allen 2021/10/8 21:19  数组实现的队列
 **/
public class ArrayQueue {
    // 数组：items，数组大小：n
    private String[] items;
    private int n = 0;
    // head表示队头下标，tail表示队尾下标
    private int head = 0;
    private int tail = 0;

    // 申请一个大小为capacity的数组
    public ArrayQueue(int capacity) {
        items = new String[capacity];
        this.n = capacity;
    }

    // 入队
    public boolean enqueue(String item){
        // 如果 tail == n 表示队列已经满了
        if (tail == n){
            return false;
        }

        items[tail] = item;
        ++tail;
        return true;
    }


    // 入队操作，将item放入队尾  ，我们只需要在入队时，再集中触发一次数据的搬移操作。
    // https://static001.geekbang.org/resource/image/09/c7/094ba7722eeec46ead58b40c097353c7.jpg
    public boolean enqueue2(String item) {
        // tail == n表示队列末尾没有空间了
        if (tail == n) {
            // tail ==n && head==0，表示整个队列都占满了
            if (head == 0) {return false;}
            // 数据搬移
            for (int i = head; i < tail; ++i) {
                items[i-head] = items[i];
            }
            // 搬移完之后重新更新head和tail
            tail -= head;
            head = 0;
        }

        items[tail] = item;
        ++tail;
        return true;
    }

    public String dequeue(){
        // 如果 head == tail 表示队列为空
        if (head == tail){
            return null;
        }

        String ret = items[head];

        ++head;
        return ret;
    }
}
