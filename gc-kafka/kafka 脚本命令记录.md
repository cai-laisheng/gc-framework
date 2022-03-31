## 监控有关命令
使用 Kafka 自带的命令行工具 bin/kafka-consumer-groups.sh(bat)。kafka-consumer-groups 脚本是 Kafka 为我们提供的最直接的监控消费者消费进度的工具

    $ bin/kafka-consumer-groups.sh --bootstrap-server <Kafka broker连接信息> --describe --group <group名称>

Kafka 连接信息就是 < 主机名：端口 > 对，而 group 名称就是你的消费者程序中设置的 group.id 值。

* 创建主题


    bin/kafka-topics.sh --bootstrap-server broker_host:port --create --topic my_topic_name  --partitions 1 --replication-factor 1

create 表明我们要创建主题，而 partitions 和 replication factor 分别设置了主题的分区数以及每个分区下的副本数。

* 查询所有主题的列表


    bin/kafka-topics.sh --bootstrap-server broker_host:port --list

* 查询单个主题的详细数据


     bin/kafka-topics.sh --bootstrap-server broker_host:port --describe --topic <topic_name>

* 修改主题分区


    bin/kafka-topics.sh --bootstrap-server broker_host:port --alter --topic <topic_name> --partitions <新分区数>

* 修改主题级别参数


    bin/kafka-configs.sh --zookeeper zookeeper_host:port --entity-type topics --entity-name <topic_name> --alter --add-config max.message.bytes=10485760

* 变更副本数/主题分区迁移

  kafka-reassign-partitions 脚本


    bin/kafka-reassign-partitions.sh --zookeeper zookeeper_host:port --reassignment-json-file reassign.json --execute


* 修改主题限速


     bin/kafka-configs.sh --zookeeper zookeeper_host:port --alter --add-config 'leader.replication.throttled.rate=104857600,follower.replication.throttled.rate=104857600' --entity-type brokers --entity-name 0
     bin/kafka-configs.sh --zookeeper zookeeper_host:port --alter --add-config 'leader.replication.throttled.replicas=*,follower.replication.throttled.replicas=*' --entity-type topics --entity-name test
所有副本都设置限速，因此统一使用通配符 * 来表示

* 如何删除主题

 
     bin/kafka-topics.sh --bootstrap-server broker_host:port --delete  --topic <topic_name>

* 查看消费者组提交的位移数据。

     
     bin/kafka-console-consumer.sh --bootstrap-server kafka_host:port --topic __consumer_offsets --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" --from-beginning

* 读取该主题消息，查看消费者组的状态信息。

 
     bin/kafka-console-consumer.sh --bootstrap-server kafka_host:port --topic __consumer_offsets --formatter "kafka.coordinator.group.GroupMetadataManager\$GroupMetadataMessageFormatter" --from-beginning
* 生产消息


     bin/kafka-console-producer.sh --broker-list kafka-host:port --topic test-topic --request-required-acks -1 --producer-property compression.type=lz4
* 消费消息

    
     bin/kafka-console-consumer.sh --bootstrap-server kafka-host:port --topic test-topic --group test-group --from-beginning --consumer-property enable.auto.commit=false 
