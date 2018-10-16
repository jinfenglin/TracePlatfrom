package spring.services;

import computationEngine.SparkJob;
import computationEngine.SparkJobEngine;
import messageDeliver.DebeziumEvent;
import messageDeliver.DebeziumReceiver;
import messageDeliver.dbSchema.DbSchema;
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
    SparkJobEngine sparkJobEngine;
    @Autowired
    DebeziumReceiver cdcReceiver;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) throws Exception {
        SparkJob exampleJob = new SparkJob("exmapleJob", "local");
        sparkJobEngine.submit(exampleJob);
    }
}
