package edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class SchedulerConfig {

    @Bean
    public Duration schedulerInterval(ApplicationConfig applicationConfig) {
        return applicationConfig.scheduler().interval();
    }
}
