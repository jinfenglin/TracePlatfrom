package spring.config;


import messageDeliver.CDCReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringRootConfig {
    @Bean
    @Scope("prototype")
    public CDCReceiver cdcReceiver() {
        return new CDCReceiver();
    }
}
