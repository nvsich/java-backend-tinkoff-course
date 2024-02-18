package edu.java.bot.configuration;

import edu.java.bot.command.Command;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.processor.UserMessageProcessorImpl;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMessageProcessorConfig {

    @Bean
    public UserMessageProcessor userMessageProcessorImpl(
        List<Command> commands,
        ChatStateRepo chatStateRepo,
        LinkRepo linkRepo,
        ApplicationContext applicationContext
    ) {
        return new UserMessageProcessorImpl(commands, chatStateRepo, linkRepo, applicationContext);
    }
}
