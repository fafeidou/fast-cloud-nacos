* https://blog.csdn.net/qq330983778/article/details/105937453

* https://blog.csdn.net/Alex19961223/article/details/104369720

* https://my.oschina.net/u/2000675/blog/3002340

容器docker中kafka的bin目录为 cd /opt/kafka/bin


1、查看消息队列挤压情况
./kafka-consumer-groups.sh --bootstrap-server 192.168.56.124:9092 --describe --group  testGroup

LogEndOffset 下一条将要被加入到日志的消息的位移
CurrentOffset 当前消费的位移
LAG 消息堆积量

2、列出topic
./kafka-topics.sh --list --zookeeper 192.168.56.124:2181
3、描述topic
./kafka-topics.sh --describe --zookeeper 192.168.56.124:2181 --topic kafka-topic2.DLT
4、


* https://blog.51cto.com/12473494/2420105 参考

* kafka manager监测不到consumer  https://blog.csdn.net/jm88621/article/details/100736645

* Spring-Kafka（六）—— @KafkaListener的花式操作 https://www.jianshu.com/p/a64defb44a23 

## 多数据源测试

* spring 单数据源测试
   * producer
   ```
     http://localhost:8060/sendStr3?message=aaa
   ```
   * consumer
  ```
   KafkaConsumerListener   : kafka-topic3接收结果:aaa
  ```

* 自定义多数据源测试
  * producer A
  ```
   http://localhost:8060/sendSourceA?message=aaa
  ```
  * producer B
  ```
  http://localhost:8060/sendSourceB?message=aaa
  ```
  * consumer A
  ```
   KafkaConsumerListener   : topicA接收结果:TopicATest(payload=aaa)
  ```
  * consumer B
  ```
   KafkaConsumerListener   : topicC接收结果:aaa
  ```

