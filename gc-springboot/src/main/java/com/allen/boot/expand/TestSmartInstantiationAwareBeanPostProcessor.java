package com.allen.boot.expand;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;

/**
 * @author xuguocai
 * @date 2022/8/16 17:04
 */
public class TestSmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

    /**
     * 该触发点发生在postProcessBeforeInstantiation之前(在图上并没有标明，因为一般不太需要扩展这个点)，这个方法用于预测Bean的类型，返回第一个预测成功的Class类型，
     * 如果不能预测返回null；当你调用BeanFactory.getType(name)时当通过bean的名字无法得到bean类型信息时就调用该回调方法来决定类型信息。
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("[TestSmartInstantiationAwareBeanPostProcessor] predictBeanType " + beanName);
        return SmartInstantiationAwareBeanPostProcessor.super.predictBeanType(beanClass, beanName);
    }

    /**
     * 该触发点发生在postProcessBeforeInstantiation之后，用于确定该bean的构造函数之用，返回的是该bean的所有构造函数列表。
     * 用户可以扩展这个点，来自定义选择相应的构造器来实例化这个bean。
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("[TestSmartInstantiationAwareBeanPostProcessor] determineCandidateConstructors " + beanName);
        return SmartInstantiationAwareBeanPostProcessor.super.determineCandidateConstructors(beanClass, beanName);
    }

    /**
     * 该触发点发生在postProcessAfterInstantiation之后，当有循环依赖的场景，当bean实例化好之后，为了防止有循环依赖，会提前暴露回调方法，
     * 用于bean实例化的后置处理。这个方法就是在提前暴露的回调方法中触发。
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        System.out.println("[TestSmartInstantiationAwareBeanPostProcessor] getEarlyBeanReference " + beanName);
        return SmartInstantiationAwareBeanPostProcessor.super.getEarlyBeanReference(bean, beanName);
    }
}
