package com.allen.kafka.config;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @author xuguocai  @date 2022/3/22 15:50  producer生产者拦截器
 */
public class AddTimeStampInterceptor implements ProducerInterceptor {

    /**
     * 该方法会在消息发送之前被调用。如果你想在发送之前对消息“美美容”，这个方法是你唯一的机会。
     * @param producerRecord
     * @return
     */
    @Override
    public ProducerRecord onSend(ProducerRecord producerRecord) {
        //todo
        return null;
    }

    /**
     * 该方法会在消息成功提交或发送失败之后被调用。
     *
     * 还记得我在上一期中提到的发送回调通知 callback 吗？onAcknowledgement 的调用要早于 callback 的调用。
     * 值得注意的是，这个方法和 onSend 不是在同一个线程中被调用的，因此如果你在这两个方法中调用了某个共享可变对象，一定要保证线程安全哦。还有一点很重要，
     * 这个方法处在 Producer 发送的主路径中，所以最好别放一些太重的逻辑进去，否则你会发现你的 Producer TPS 直线下降。
     * @param recordMetadata
     * @param e
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        //todo
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
