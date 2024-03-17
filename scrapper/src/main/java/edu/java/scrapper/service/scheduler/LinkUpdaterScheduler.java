package edu.java.scrapper.service.scheduler;

import edu.java.scrapper.service.processor.LinkUpdater;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@AllArgsConstructor
public class LinkUpdaterScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@schedulerInterval.toMillis()}")
    public void update() {
        var updatesCount = linkUpdater.update();
        LOGGER.info(String.format("LinkUpdaterScheduler#update() called. %d updates handled", updatesCount));
    }
}
