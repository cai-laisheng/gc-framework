package com.allen.thread.keyword;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: MultiThread
 * @description: FinalDemo
 * @author: allen小哥
 * @Date: 2019-11-29 18:58
 **/
@Slf4j
public class FinalReferenceDemo {

    final int[] arrays;
    private FinalReferenceDemo finalReferenceDemo;

    public FinalReferenceDemo() {
        arrays = new int[1];  //1
        arrays[0] = 1;        //2
    }

    public void writerOne() {
        finalReferenceDemo = new FinalReferenceDemo(); //3
    }

    public void writerTwo() {
        arrays[0] = 2;  //4
    }

    public void reader() {
        if (finalReferenceDemo != null) {  //5
            int temp = finalReferenceDemo.arrays[0];  //6
        }
    }
}
