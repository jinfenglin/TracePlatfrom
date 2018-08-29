package messageDeliver;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

/**
 *
 */
public class DebeziumReceiver<K, V> extends CDCReceiver<K, V> {
    public DebeziumReceiver() {
        super();
    }
}
