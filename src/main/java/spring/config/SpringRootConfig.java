package spring.config;


import messageDeliver.CDCReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:KafkaConsumer.properties")
public class SpringRootConfig {
    @Autowired
    private Environment environment;

    @Bean
    @Scope("prototype")
    public CDCReceiver cdcReceiver() {
        String groupId = environment.getProperty("group.id");
        String servers = environment.getProperty("bootstrap.servers");
        String autoCommit = environment.getProperty("enable.auto.commit");
        List<String> topics = Arrays.asList(environment.getProperty("topics").split(","));
        String pollTimeout = environment.getProperty("timeout.poll.seconds");
        return new CDCReceiver(servers, groupId, autoCommit, pollTimeout, topics);
    }
}
