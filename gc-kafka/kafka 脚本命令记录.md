## 监控有关命令
使用 Kafka 自带的命令行工具 bin/kafka-consumer-groups.sh(bat)。kafka-consumer-groups 脚本是 Kafka 为我们提供的最直接的监控消费者消费进度的工具

    $ bin/kafka-consumer-groups.sh --bootstrap-server <Kafka broker连接信息> --describe --group <group名称>

Kafka 连接信息就是 < 主机名：端口 > 对，而 group 名称就是你的消费者程序中设置的 group.id 值。


