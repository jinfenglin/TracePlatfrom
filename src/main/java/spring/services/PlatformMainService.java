package spring.services;

import computationEngine.Artifact;
import computationEngine.Model.TraceModel;
import computationEngine.Model.TraceModelManger;
import computationEngine.Model.TraceModelType;
import computationEngine.SparkJob;
import computationEngine.SparkJobEngine;
import messageDeliver.DebeziumEvent;
import messageDeliver.DebeziumReceiver;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import py4j.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    }

    @EventListener
    public void handleDebeziumEvent(DebeziumEvent event) {
        TraceModel model = traceModelManger.getModel(TraceModelType.DUMMY);
        String sparkUrl = environment.getProperty("spark.master");
        SparkJob sparkJob = sparkJobEngine.getSparkJobMaker().createJobFromEvent(event, sparkUrl, model);
        sparkJobEngine.submit(sparkJob);
    }

}
