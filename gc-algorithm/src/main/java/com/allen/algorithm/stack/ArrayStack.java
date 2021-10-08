package com.allen.algorithm.stack;

/**
 * @Author Allen 2021/9/28 9:17  基于数组的顺序栈
 **/
public class ArrayStack {
    // 数组
    private String[] iems;
    // 栈中元素个数
    private int count;
    // 栈的大小
    private int n;

    // 初始化数组，申请一个大小为n的数组 空间
    public ArrayStack(int n) {
        this.iems = new String[n];
        this.n = n;
        this.count =0;
    }

    // 入栈
    public boolean push(String item){
        if (count == n){
            //数组空间不够，直接返回 false，入栈失败
            return false;
        }

        iems[count] = item;
        ++ count;
        return true;
    }

    // 出栈
    public String pop(){
        if (count == 0){
            // 栈空，则直接返回空
            return null;
        }
        // 返回下标为count-1的数组元素，并且栈中元素个数count减一
        String tmp = iems[count-1];

        --count;

        return tmp;

    }
}
