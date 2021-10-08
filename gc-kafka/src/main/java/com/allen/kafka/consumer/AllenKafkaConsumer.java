package com.allen.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 验证 kafka 是否 正常消费，部署前需要注释 @KafkaListener
 * @author xuguocai 2020/8/17 14:55
 */
@Component
public class AllenKafkaConsumer {
    private Logger log = LoggerFactory.getLogger(AllenKafkaConsumer.class);

    /**
     * enable-auto-commit = true  不重复消费
     *
     * 自动提交
     * @param record
     */
//    @KafkaListener(topics = "allen-test-topic",groupId = "allenGroup")
    public void pullMessage(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("自动提交消息体：{}",message);
        }
    }

    /**
     * enable-auto-commit = false  手动提交 单条消费，同步提交  存在循环重复消费
     * @param record
     * @param ack
     *
     * 方法参数：pullMessage(@Payload String record, Acknowledgment ack)
     *         pullMessage(ConsumerRecord<String,String> record, Acknowledgment ack)
     */
//    @KafkaListener(containerFactory = "ackSimpleFactory",groupId = "allenGroup1",topics = "qywx-ucm-externalUserMsg")
    public void pullMessage(@Payload String record, Acknowledgment ack) {
        // 手动提交
        if (ack !=null){
            log.info("pullMessage-------手动提交:{}",record);
            ack.acknowledge();
        }
    }

    /**
     * enable-auto-commit = false  批量消费 。需指定对应bean 配置 ，手动同步提交。不存在重复消费
     * @param records
     * @param ack
     */
    @KafkaListener(containerFactory = "batchFactory",groupId = "allenGroup2",topics = "qywx-ucm-externalUserMsg")
    public void pullMoreMessage(List<ConsumerRecord<String,String>> records, Acknowledgment ack) {
        log.info("批量消息大小:{}",records.size());
        for (ConsumerRecord  item: records){
            Optional<?> kafkaMessage = Optional.ofNullable(item.value());
            if (kafkaMessage.isPresent()) {
                Object message = kafkaMessage.get();

//                log.info("批量消息体：{}",message);

                // 处理消息 todo 业务处理
            }
        }

        // 手动提交  默认使用 commitSync 同步提交
        if (ack !=null){
            log.info("批量消息体提交");
            ack.acknowledge();
        }
    }

    /**
     * 异步提交 ，单条消费。存在重复消费
     * @param message
     * @param partition
     * @param topic
     * @param offset
     * @param consumer
     */
//    @KafkaListener(topics = "qywx-ucm-externalUserMsg",groupId = "allenGroup3")
    public void onMessage(@Payload String message,
                          @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.OFFSET) String offset,
                          Consumer consumer) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println(String.format("消费：topic:%s-partition:%s-offset:%s-value:%s", topic,partition,offset,message));
        // 异步提交
//        consumer.commitAsync();
        // 同步提交
        consumer.commitSync();
    }
}
