package spring.services;

import messageDeliver.CDCReceiver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

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
        CDCReceiver<String, String> receiver = (CDCReceiver) applicationContext.getBean("cdcReceiver");
        while (true) {

            ConsumerRecords<String, String> records = receiver.getRecords();
            System.out.println(String.format("%s records polled...", records.count()));
            for (ConsumerRecord record : records) {
                System.out.println(record.offset() + ": " + record.value());
            }

        }
    }
}
