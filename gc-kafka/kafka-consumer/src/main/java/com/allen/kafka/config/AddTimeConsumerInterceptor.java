package com.allen.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Map;

/**
 * @author xuguocai  @date 2022/3/22 16:15
 */
public class AddTimeConsumerInterceptor implements ConsumerInterceptor {

    /**
     * 该方法在消息返回给 Consumer 程序之前调用。也就是说在开始正式处理消息之前，拦截器会先拦一道，搞一些事情，之后再返回给你
     *
     * @param consumerRecords
     * @return
     */
    @Override
    public ConsumerRecords onConsume(ConsumerRecords consumerRecords) {
        // todo
        return null;
    }

    /**
     * Consumer 在提交位移之后调用该方法。通常你可以在该方法中做一些记账类的动作，比如打日志等。
     * @param map
     */
    @Override
    public void onCommit(Map map) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
