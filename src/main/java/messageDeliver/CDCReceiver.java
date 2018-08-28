package messageDeliver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Receive the events from the CDC(Change Data Capture) tools - Debezium. As this tool is based on Kafka, this class
 * wrap a Kafka Consumer object.
 */
@PropertySource("classpath:KafkaConsumer.properties")
public class CDCReceiver<K, V> {
    @Value("group.id")
    private String groupId;
    @Value("bootstrap.servers")
    private String servers;
    @Value("enable.auto.commit")
    private String autoCommit;
    @Value("#{'${topics}'.split(',')}")
    private List<String> topics;

    @Value("timeout.poll.seconds")
    private String pollTimeout;

    private KafkaConsumer<K, V> consumer;

    public CDCReceiver() {
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", autoCommit);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(topics);
        System.out.println("Subscribed to topic " + topics.toString());
    }

    public KafkaConsumer<K, V> getConsumer() {
        return consumer;
    }

    public ConsumerRecords<K, V> getRecords() {
        return consumer.poll(Duration.ofSeconds(Long.valueOf(pollTimeout)));
    }


}
