package spring.services;

import computationEngine.Model.TraceModel;
import computationEngine.Model.TraceModelManger;
import computationEngine.Model.TraceModelType;
import computationEngine.SparkJobs.LinkGenerationSparkJob;
import computationEngine.SparkJobEngine.SparkJobEngine;
import messageDeliver.DebeziumEvent;
import messageDeliver.DebeziumReceiver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @EventListener()
    public void OnApplicationStart(ContextRefreshedEvent event) throws Exception {
        boolean debug = Boolean.valueOf(environment.getProperty("platform.debug"));
        logger.info("Start Platform main service...");
        traceModelManger.startModelMonitor();
        cdcReceiver.startEventPublishService();
        sparkJobEngine.start();
    }

    @EventListener
    public void handleDebeziumEvent(DebeziumEvent event) {
        TraceModel model = traceModelManger.getModel(TraceModelType.DUMMY);
        String sparkUrl = environment.getProperty("spark.master");
        LinkGenerationSparkJob sparkJob = sparkJobEngine.getSparkJobMaker().createLinkGenJobFromEvent(event, sparkUrl, model);
        sparkJobEngine.submit(sparkJob);
    }

}
