package spring.config;


import computationEngine.Model.ModelUpdatePolicy.DummyPolicy;
import computationEngine.Model.ModelUpdatePolicy.TModelUpdatePolicy;
import computationEngine.Model.ModelUpdatePolicy.UpdatePolicyType;
import computationEngine.Model.TraceModelManger;
import messageDeliver.DebeziumReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource({"classpath:KafkaConsumer.properties", "classpath:platform.properties"})
public class SpringRootConfig {
    @Autowired
    private Environment environment;

    @Bean
    @Scope("prototype")
    public DebeziumReceiver cdcReceiver() {
        String groupId = environment.getProperty("group.id");
        String servers = environment.getProperty("bootstrap.servers");
        String autoCommit = environment.getProperty("enable.auto.commit");
        List<String> topics = Arrays.asList(environment.getProperty("topics").split(","));
        String pollTimeout = environment.getProperty("timeout.poll.seconds");
        return new DebeziumReceiver(servers, groupId, autoCommit, pollTimeout, topics);
    }


    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        return executor;
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
}
