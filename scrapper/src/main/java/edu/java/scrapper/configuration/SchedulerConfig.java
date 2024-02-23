package edu.java.scrapper.configuration;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

    @Bean
    public Duration schedulerInterval(ApplicationConfig applicationConfig) {
        return applicationConfig.scheduler().interval();
    }
}
