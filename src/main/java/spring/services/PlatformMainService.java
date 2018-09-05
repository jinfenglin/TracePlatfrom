package spring.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import messageDeliver.DebeziumReceiver;
import messageDeliver.DebeziumEvent;
import messageDeliver.dbSchema.DbSchema;
import messageDeliver.gsonDeserializer.DebeziumJsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Entrance of the TracePlatform. The background services will be activated after the application start.
 * It will start the Kafka consumers
 */
@Component
public class PlatformMainService {
    @Autowired
    private ApplicationContext applicationContext;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) throws Exception {
        DebeziumReceiver<String, String> receiver = (DebeziumReceiver) applicationContext.getBean("cdcReceiver");
        while (true) {
            List<DebeziumEvent> eventList = receiver.getEvents();
            System.out.println(String.format("receiving %s events", eventList.size()));
            for (DebeziumEvent debeziumEvent : eventList) {
                DbSchema schema = debeziumEvent.getBeforeAsDbSchema();
                System.out.println(schema.toString());
            }
        }
    }
}
