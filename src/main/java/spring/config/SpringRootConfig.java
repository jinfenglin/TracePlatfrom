package spring.config;


import computationEngine.Model.ModelUpdatePolicy.DummyPolicy;
import computationEngine.Model.ModelUpdatePolicy.TModelUpdatePolicy;
import computationEngine.Model.ModelUpdatePolicy.UpdatePolicyType;
import computationEngine.Model.TraceModelManger;
import computationEngine.SparkJobs.SparkJob;
import messageDeliver.DebeziumReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import spring.services.TIMGraph;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@PropertySource({"classpath:KafkaConsumer.properties", "classpath:platform.properties"})
@ComponentScan({"spring.controller", "spring.services", "messageDeliver", "computationEngine", "computationEngine.SparkJobEngine"})
public class SpringRootConfig {
    @Autowired
    private Environment environment;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Bean
    @Scope("prototype")
    public DebeziumReceiver cdcReceiver() {
        String groupId = environment.getProperty("group.id");
        String servers = environment.getProperty("bootstrap.servers");
        String autoCommit = environment.getProperty("enable.auto.commit");
        List<String> topics = Arrays.asList(environment.getProperty("topics").split(","));
        String pollTimeout = environment.getProperty("timeout.poll.seconds");
        return new DebeziumReceiver(servers, groupId, autoCommit, pollTimeout, topics, applicationEventPublisher);
    }


    @Bean
    public CompletionService threadPoolTaskExecutor() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        return new ExecutorCompletionService<SparkJob>(executor);
    }

    @Bean
    TraceModelManger traceModelManger() {
        UpdatePolicyType policyType = UpdatePolicyType.valueOf(environment.getProperty("traceModelManger.policy"));
        TModelUpdatePolicy policy = null;
        switch (policyType) {
            case DUMMY:
                policy = new DummyPolicy();
                break;
            case CONSTANT:
                break;
            case CONDITIONAL:
                break;
            case NEW_DATA_DRIVEN:
                break;
        }
        return new TraceModelManger(policy);
    }

    @Bean
    TIMGraph timGraph() throws IOException {
        String TIMFileName = environment.getProperty("platform.TIM.fileName");
        return new TIMGraph(TIMFileName);
    }

}
