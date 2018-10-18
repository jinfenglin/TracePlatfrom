package spring.services;

import computationEngine.Model.TraceModel;
import computationEngine.Model.TraceModelManger;
import computationEngine.Model.TraceModelType;
import computationEngine.SparkJob;
import computationEngine.SparkJobEngine;
import messageDeliver.DebeziumEvent;
import messageDeliver.DebeziumReceiver;
import messageDeliver.dbSchema.DbSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Entrance of the TracePlatform. The background services will be activated after the application start.
 * It will start the Kafka consumers
 */
@Component
@PropertySource({"classpath:platform.properties"})
public class PlatformMainService {
    @Autowired
    private SparkJobEngine sparkJobEngine;
    @Autowired
    private DebeziumReceiver cdcReceiver;
    @Autowired
    private TraceModelManger traceModelManger;

    @Autowired
    private Environment environment;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) throws Exception {
        traceModelManger.startModelMonitor();
        TraceModel model = traceModelManger.getModel(TraceModelType.DUMMY);
        String sparkUrl = environment.getProperty("spark.master");
        boolean debug = Boolean.valueOf(environment.getProperty("platform.debug"));
        if (debug) {
            SparkJob exampleJob = new SparkJob("exmapleJob", "local", model);
            sparkJobEngine.submit(exampleJob);
        } else {
            while (true) {
                List<DebeziumEvent> eventList = cdcReceiver.getEvents();
                System.out.println(String.format("receiving %s events", eventList.size()));
                for (DebeziumEvent debeziumEvent : eventList) {
                    //TODO The spark job scheduling functionality.
                    // If user is querying a artifact which have outdated links, the jobs contains that artifact should
                    // have higehr priority. This functionality should be provided by SparkJobEngine
                    SparkJob sparkJob = createJobFromEvent(debeziumEvent);
                    sparkJobEngine.submit(sparkJob);
                }
            }
        }
    }

    /**
     * Given an event, base on its operation code, this method find the modifications on source artifact and respectively
     * find the impacted target artifacts.
     *
     * @param event
     * @return
     */
    private SparkJob createJobFromEvent(DebeziumEvent event) {
        switch (event.getOpCode()) {
            case "c"://create
                break;
            case "d"://delete
                break;
            case "u"://udpate
                break;
        }
        return null;
    }
}
