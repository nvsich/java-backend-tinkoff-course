package edu.java.scrapper.service.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class LinkUpdaterScheduler {
    private static final Logger logger = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    @Scheduled(fixedDelayString = "#{@schedulerInterval.toMillis()}")
    public void update() {
        logger.info("LinkUpdaterScheduler#update() stub called");
    }
}
