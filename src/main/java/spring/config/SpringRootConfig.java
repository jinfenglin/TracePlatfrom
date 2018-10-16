package spring.config;


import computationEngine.SparkJobEngine;
import messageDeliver.DebeziumReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:KafkaConsumer.properties")
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

}
