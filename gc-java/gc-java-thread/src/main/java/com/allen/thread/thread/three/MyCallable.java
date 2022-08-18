package com.allen.thread.thread.three;

import java.util.concurrent.Callable;

/**
 * @author xuguocai
 * @date 2022/8/18 17:59
 */
public class MyCallable implements Callable {

    @Override
    public Object call() throws Exception {
        String name = Thread.currentThread().getName();
        System.out.println(name + "已经运行MyCallable");
        return null;
    }
}
