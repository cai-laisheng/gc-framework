package com.allen.boot.expand;

import org.springframework.beans.factory.BeanNameAware;

/**
 * @author xuguocai
 * @date 2022/8/16 17:20
 *
 * 这个类也是Aware扩展的一种，触发点在bean的初始化之前，也就是postProcessBeforeInitialization之前，这个类的触发点方法只有一个：setBeanName
 *
 * 使用场景为：用户可以扩展这个点，在初始化bean之前拿到spring容器中注册的的beanName，来自行修改这个beanName的值。
 */
public class TestBeanNameAware implements BeanNameAware {

    public TestBeanNameAware() {
        System.out.println("NormalBean constructor");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("[BeanNameAware] " + name);
    }
}
