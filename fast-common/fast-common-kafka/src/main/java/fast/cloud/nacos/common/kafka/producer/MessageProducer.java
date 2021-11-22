package fast.cloud.nacos.common.kafka.producer;

public interface MessageProducer {

    /**
     * 发送信息
     *
     * @param message
     * @throws RuntimeException
     */
    void sendMessage(Object message) throws RuntimeException;

    /**
     * 发送信息
     *
     * @param topicEx kafka的tpic或者rabbit的exchange
     */
    void sendMessage(String topicEx, Object message) throws RuntimeException;

    /**
     * 发送信息
     *
     * @param topicEx kafka的tpic或者rabbit的exchange
     * @param key     kafka的partionKey或者rabbit的RoutingKey
     * @param message
     * @throws RuntimeException
     */
    void sendMessage(String topicEx, String key, Object message) throws RuntimeException;


}
