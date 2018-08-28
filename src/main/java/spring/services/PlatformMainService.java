package spring.services;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Entrance of the TracePlatform. The background services will be activated after the application start.
 * It will start the Kafka consumers
 */
@Component
public class PlatformMainService {

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {

    }
}
