package com.allen.kafka.listener;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

/**
 * @Author Allen 2022/3/13 15:11
 *
 *  KafkaStreamListener的泛型是固定的，有两种泛型可以选择
 *  KTable
 *  KStream
 *
 *  手动创建监听器
 *
 * 1,该类需要实现KafkaStreamListener接口
 *
 * 2,listenerTopic方法返回需要监听的topic
 *
 * 3,sendTopic方法返回需要处理完后发送的topic
 *
 * 4,getService方法，主要处理流数据
 **/
@Component
public class StreamHandlerListener  implements KafkaStreamListener<KStream<String,String>>{

    /**
     * 在哪里接收消息  源处理器
     * @return
     */
    @Override
    public String listenerTopic() {
        return "input_topic";
    }

    /**
     * 计算完成后的结果发送到什么位置  下游处理器
     * @return
     */
    @Override
    public String sendTopic() {
        return "out_topic";
    }

    @Override
    public KStream<String, String> getService(KStream<String, String> stream) {
        //计算
        return stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            /**
             * 把消息中的词组，转换为一个一个的单词放到集合中
             * @param value
             * @return
             */
            @Override
            public Iterable<String> apply(String value) {
                System.out.println("消息的value:"+value);//hello kafka stareams
                String[] strings = value.split(" ");
                return Arrays.asList(strings);
            }
        }).map(new KeyValueMapper<String, String, KeyValue<String, String>>() {
            /**
             * 把消息的key,重新赋值，目前消息的key就是单词
             * @param key
             * @param value
             * @return
             */
            @Override
            public KeyValue<String, String> apply(String key, String value) {
                return new KeyValue<>(value,value);
            }
        }).groupByKey()
                //时间聚合窗口
                .windowedBy(TimeWindows.of(Duration.ofSeconds(5000)))
                //消息的value就是聚合单词后的统计数值，long类型
                .count(Materialized.as("count-word-num-0001"))
                //转换为Kstream
                .toStream()
                //把处理后的key和value转换String
                .map((key,value)->{
                    return new KeyValue<>(key.key().toString(),value.toString());
                });

    }
}
