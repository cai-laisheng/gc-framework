## 配置及参数说明

### consumer的配置参数
  
    #如果'enable.auto.commit'为true，则消费者偏移自动提交给Kafka的频率（以毫秒为单位），默认值为5000。
    spring.kafka.consumer.auto-commit-interval = 5000;
    
    #当Kafka中没有初始偏移量或者服务器上不再存在当前偏移量时该怎么办，默认值为latest，表示自动将偏移重置为最新的偏移量
    #可选的值为latest, earliest, none
    # earliest： 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
    # latest： 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
    # none： topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
    spring.kafka.consumer.auto-offset-reset=latest;
    
    #以逗号分隔的主机：端口对列表，用于建立与Kafka群集的初始连接。
    spring.kafka.consumer.bootstrap-servers = 192.168.240.42:9092,192.168.240.43:9092,192.168.240.44:9092;
    
    #ID在发出请求时传递给服务器;用于服务器端日志记录。
    spring.kafka.consumer.client-id;
    
    #如果为true，则消费者的偏移量将在后台定期提交，默认值为true
    spring.kafka.consumer.enable-auto-commit=true;
    
    #如果没有足够的数据立即满足“fetch.min.bytes”给出的要求，服务器在回答获取请求之前将阻塞的最长时间（以毫秒为单位）
    #默认值为500
    spring.kafka.consumer.fetch-max-wait = 500;
    
    #服务器应以字节为单位返回获取请求的最小数据量，默认值为1，对应的kafka的参数为fetch.min.bytes。
    spring.kafka.consumer.fetch-min-size = 1;
    
    #用于标识此使用者所属的使用者组的唯一字符串。
    spring.kafka.consumer.group-id = allenGroup;
    
    #心跳与消费者协调员之间的预期时间（以毫秒为单位），默认值为3000
    spring.kafka.consumer.heartbeat-interval = 3000;
    
    #密钥的反序列化器类，实现类实现了接口org.apache.kafka.common.serialization.Deserializer
    spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
    
    #值的反序列化器类，实现类实现了接口org.apache.kafka.common.serialization.Deserializer
    spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
    
    #一次调用poll()操作时返回的最大记录数，默认值为500
    spring.kafka.consumer.max-poll-records;


### producer的配置参数

    #procedure要求leader在考虑完成请求之前收到的确认数，用于控制发送记录在服务端的持久化，其值可以为如下：
    #acks = 0 如果设置为零，则生产者将不会等待来自服务器的任何确认，该记录将立即添加到套接字缓冲区并视为已发送。
    在这种情况下，无法保证服务器已收到记录，并且重试配置将不会生效（因为客户端通常不会知道任何故障），为每条记录返回的偏移量始终设置为-1。
    #acks = 1 这意味着leader会将记录写入其本地日志，但无需等待所有副本服务器的完全确认即可做出回应，
    在这种情况下，如果leader在确认记录后立即失败，但在将数据复制到所有的副本服务器之前，则记录将会丢失。
    #acks = all 这意味着leader将等待完整的同步副本集以确认记录，这保证了只要至少一个同步副本服务器仍然存活，记录就不会丢失，这是最强有力的保证，这相当于acks = -1的设置。
    #可以设置的值为：all, -1, 0, 1
    spring.kafka.producer.acks=1
    
    #每当多个记录被发送到同一分区时，生产者将尝试将记录一起批量处理为更少的请求，
    #这有助于提升客户端和服务器上的性能，此配置控制默认批量大小（以字节为单位），默认值为16384
    spring.kafka.producer.batch-size=16384
    
    #以逗号分隔的主机：端口对列表，用于建立与Kafka群集的初始连接
    spring.kafka.producer.bootstrap-servers = 192.168.240.42:9092,192.168.240.43:9092,192.168.240.44:9092;
    
    #生产者可用于缓冲等待发送到服务器的记录的内存总字节数，默认值为33554432
    spring.kafka.producer.buffer-memory=33554432
    
    #ID在发出请求时传递给服务器，用于服务器端日志记录
    spring.kafka.producer.client-id
    
    #生产者生成的所有数据的压缩类型，此配置接受标准压缩编解码器（'gzip'，'snappy'，'lz4'），
    #它还接受'uncompressed'以及'producer'，分别表示没有压缩以及保留生产者设置的原始压缩编解码器，
    #默认值为producer
    spring.kafka.producer.compression-type=producer
    
    #key的Serializer类，实现类实现了接口org.apache.kafka.common.serialization.Serializer
    spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
    
    #值的Serializer类，实现类实现了接口org.apache.kafka.common.serialization.Serializer
    spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
    
    #如果该值大于零时，表示启用重试失败的发送次数
    spring.kafka.producer.retries = 3
  
### listener的配置参数
   
    #侦听器的AckMode,参见https://docs.spring.io/spring-kafka/reference/htmlsingle/#committing-offsets
    #当enable.auto.commit的值设置为false时，该值会生效；为true时不会生效。AckMode取值入下：
    # MANUAL 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后, 手动调用Acknowledgment.acknowledge()后提交
    # MANUAL_IMMEDIATE  手动调用Acknowledgment.acknowledge()后立即提交
    # RECORD  当每一条记录被消费者监听器（ListenerConsumer）处理之后提交
    # BATCH   当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后提交
    # TIME  当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，距离上次提交时间大于TIME时提交
    # COUNT  当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，被处理record数量大于等于COUNT时提交
    # COUNT_TIME  TIME或COUNT　有一个条件满足时提交
    spring.kafka.listener.ack-mode ;
    
    #在侦听器容器中运行的线程数
    spring.kafka.listener.concurrency;
    
    #轮询消费者时使用的超时（以毫秒为单位）
    spring.kafka.listener.poll-timeout;
    
    #当ackMode为“COUNT”或“COUNT_TIME”时，偏移提交之间的记录数
    spring.kafka.listener.ack-count;
    
    #当ackMode为“TIME”或“COUNT_TIME”时，偏移提交之间的时间（以毫秒为单位）
    spring.kafka.listener.ack-time;
   
### 重要配置（去掉默认）
    # 集群地址
    spring.kafka.bootstrap-servers=172.17.35.141:9092,172.17.41.159:9092,172.17.38.154:9092,172.17.40.60:9092
    # 消费者配置
    spring.kafka.consumer.topic=test_topic
    spring.kafka.consumer.group-id=streamProcesser
    spring.kafka.consumer.auto-offset-reset=earliest
    spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
    spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
    # 生产者配置
    spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
    spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

### 其他
* session.timeout.ms 会话超时时间
  
检测消费者是否超时故障的超时时间。group coordinator检测consumer发生崩溃所需的时间。一个consumer group里面的某个consumer挂掉了，最长需要 session.timeout.ms 秒检测出来。

机制：消费者 定期发送心跳证明自己的存活，如果在这个时间之内broker没收到，那broker就将此消费者从group中移除，进行一次reblance。

注意：此值的配置需要在group.min.session.timeout.ms 和 group.max.session.timeout.ms 范围内。


* heartbeat.interval.ms 心跳间隔时间
  
  当消费者使用group时，用于确保消费者存活，并在消费者加入或离开group时促进reblance。 每个consumer 都会根据 heartbeat.interval.ms 参数指定的时间周期性地向group coordinator发送 hearbeat，group coordinator会给各个consumer响应，若发生了 rebalance，各个consumer收到的响应中会包含 REBALANCE_IN_PROGRESS 标识，这样各个consumer就知道已经发生了rebalance，同时 group coordinator也知道了各个consumer的存活情况。

**注意：必须小于session.timeout.ms,但通常又不能大于session.timeout.ms的1/3,越小重新平衡的时间越短**

* max.poll.interval.ms  最大拉取间隔时间
  
检测消费者是否pull超时或失败.如果消费者两次pull的时间超过了此值,那就认为此消费者能力不足，将此消费者的commit标记为失败，并将此消费者从group移除，触发一次reblance,将该消费者消费的分区分配给其他人。

**注意：该值越大，reblance的时间越长。**

**所以这三个参数的目的是保证group中都是能正常消费的消费者：**

1、通过心跳判断：消费者隔heartbeat.interval.ms向broker汇报一次心跳，broker计算消费者多久没有向自己发心跳了，如果超过了session.timeout.ms，那么就认为该消费者不可用了，将其移除。

2、通过pull()时间间隔判断：broker如果发现max.poll.interval.ms没有调用pull()方法，那么就将此消费者移除。

那么有同学可能会问了：如果通过心跳判断消费者没有死，但是通过pull超时的，那么会移除么？

虽然从0.10.1以后session.timeout.ms 和 max.poll.interval.ms 解耦了，可以在处理消息的同时发送心跳，在处理消息的时候不被移除，但是当处理完毕再次调用pull方法时发现此消费者的两次pull是超时的，仍然会将其做失败的重试处理，销毁旧线程，从线程池取新线程，所以答案是会移除。


1、session.timeout.ms一定要大于heartbeat.interval.ms，否则消费者组会一直处于rebalance状态

2、session.timeout.ms最好几倍于heartbeat.interval.ms；这是因为如果因为某一时间段的网络延迟导致coordinator未感知到心跳请求，session.timeout.ms和heartbeat.interval.ms接近的话，会导致consumer组rebalance过于频繁，影响消费性能





https://blog.csdn.net/fenglibing/article/details/82117166

https://kafka.apache.org/documentation/#introduction

https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.integration.spring.kafka.admin.client-id