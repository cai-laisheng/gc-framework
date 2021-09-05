package com.allen.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Allen 2021/9/4 18:36   此处为手动提交配置
 **/
@Configuration
public class AckModeKafkaConfig {

    private Logger log = LoggerFactory.getLogger(AckModeKafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Value("${spring.kafka.consumer.session.timout.ms}")
    private String sessionTimeout;

    @Value("${spring.kafka.consumer.max.poll.interval.ms}")
    private String pollInterval;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private String pollRecords;

    @Value("${spring.kafka.consumer.heartbeat.interval.ms}")
    private String heartbeatInterval;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * kafka配置参数
     * @return
     */
    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>(9);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, pollInterval);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, pollRecords);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     * 手动提交 单条操作
     * @param consumerFactory
     * @return
     */
    @Bean("ackSimpleFactory")
    public ConcurrentKafkaListenerContainerFactory ackSimpleFactory(ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Integer,String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // 回调函数经常用于记录提交错误
        factory.getContainerProperties().setCommitCallback((offsets, exception) -> {
            if (exception != null) {
                log.error("Commit failed for offsets {}", offsets, exception);
            }
        });
        // 设置手动提交
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    /**
     * //批量消息
     * @param consumerFactory
     * @return
     */
    @Bean("batchFactory")
    public KafkaListenerContainerFactory<?> batchFactory(ConsumerFactory consumerFactory){
        ConcurrentKafkaListenerContainerFactory<Integer,String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(10);
        factory.getContainerProperties().setPollTimeout(3000);
        // 回调函数经常用于记录提交错误
        factory.getContainerProperties().setCommitCallback((offsets, exception) -> {
            if (exception != null) {
                log.error("Commit failed for offsets {}", offsets, exception);
            }
        });
        factory.setBatchListener(true);//设置为批量消费，每个批次数量在Kafka配置参数中设置
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);//设置手动提交ackMod
        return factory;
    }

}
