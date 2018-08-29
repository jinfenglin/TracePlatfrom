package messageDeliver;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Receive the events from the CDC(Change Data Capture) tools - Debezium. As this tool is based on Kafka, this class
 * wrap a Kafka Consumer object.
 * https://github.com/abhirockzz/ -javaee-test-drive
 */
@PropertySource("classpath:KafkaConsumer.properties")
public class CDCReceiver<K, V> {
    private KafkaConsumer<K, V> consumer;
    private String pollTimeout;
    private static final int ADMIN_CLIENT_TIMEOUT_MS = 5000;

    public CDCReceiver() {

    }

    public CDCReceiver(String servers, String groupId, String autoCommit, String pollTimout, List<String> topics) {
        this.pollTimeout = pollTimout;
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        //props.put("zookeeper.connect", "localhost:2181");//todo add to porperties
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());


        try (AdminClient client = AdminClient.create(props)) {
            Set<String> avaiTopics = client.listTopics(new ListTopicsOptions().timeoutMs(ADMIN_CLIENT_TIMEOUT_MS)).names().get();
            for (String name : avaiTopics) {
                System.out.println(name);
            }
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            System.out.println(String.format("Kafka is not available, timed out after %s ms", ADMIN_CLIENT_TIMEOUT_MS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(topics);
        System.out.println("Subscribed to topic " + topics.toString());
    }

    protected KafkaConsumer<K, V> getConsumer() {
        return consumer;
    }

    public ConsumerRecords<K, V> getRecords() {
        return consumer.poll(Duration.ofSeconds(Long.valueOf(pollTimeout)));
    }
}
