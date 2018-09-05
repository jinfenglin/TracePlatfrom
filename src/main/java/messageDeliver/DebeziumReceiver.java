package messageDeliver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import messageDeliver.gsonDeserializer.DebeziumJsonDeserializer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.PropertySource;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Receive the events from the CDC(Change Data Capture) tools - Debezium. As this tool is based on Kafka, this class
 * wrap a Kafka Consumer object.
 * https://github.com/abhirockzz/ -javaee-test-drive
 */
@PropertySource("classpath:KafkaConsumer.properties")
public class DebeziumReceiver<K, V> {
    private KafkaConsumer<K, V> consumer;
    private String pollTimeout;
    private static final int ADMIN_CLIENT_TIMEOUT_MS = 5000;

    Gson gson;

    public DebeziumReceiver(String servers, String groupId, String autoCommit, String pollTimout, List<String> topics) {
        this.pollTimeout = pollTimout;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.registerTypeAdapter(DebeziumEvent.class, new DebeziumJsonDeserializer()).create();

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
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

    private ConsumerRecords<K, V> getRecords() {
        return consumer.poll(Duration.ofSeconds(Long.valueOf(pollTimeout)));
    }

    public List<DebeziumEvent> getEvents() {
        List<DebeziumEvent> events = new ArrayList<>();
        for (ConsumerRecord<K, V> record : getRecords()) {
            DebeziumEvent debeziumEvent = gson.fromJson(record.value().toString(), DebeziumEvent.class);
            events.add(debeziumEvent);
        }
        return events;
    }
}
