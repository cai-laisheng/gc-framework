package com.allen.boot.expand;

import org.springframework.beans.factory.DisposableBean;

/**
 * @author xuguocai
 * @date 2022/8/16 17:32
 */
public class TestDisposableBean implements DisposableBean {

    /**
     * destroy()，其触发时机为当此对象销毁时，会自动执行这个方法。比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("[DisposableBean] TestDisposableBean");
    }
}
