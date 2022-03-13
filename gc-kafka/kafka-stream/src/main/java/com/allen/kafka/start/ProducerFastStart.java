package com.allen.kafka.start;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @Author Allen 2022/3/13 14:45
 *
 * 消息生产者
 **/
public class ProducerFastStart {
    private static final String INPUT_TOPIC="input_topic";

    public static void main(String[] args) {

        //添加kafka的配置信息
        Properties properties = new Properties();
        //配置broker 生产者信息
        properties.put("bootstrap.servers","192.168.200.130:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.RETRIES_CONFIG,10);

        //生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        try {
            //封装消息
            for (int i = 0; i < 10; i++) {
                if (i % 2 == 0) {
                    ProducerRecord<String,String> record =
                            new ProducerRecord<String, String>(INPUT_TOPIC,i+"","hello shanghai kafka stream hello");
                    //发送消息
                    producer.send(record);
                    System.out.println("发送消息："+record);
                }else {
                    ProducerRecord<String,String> record =
                            new ProducerRecord<String, String>(INPUT_TOPIC,i+"","helloworld kafka stream");
                    //发送消息
                    producer.send(record);
                    System.out.println("发送消息："+record);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //关系消息通道
        producer.close();

    }
}
