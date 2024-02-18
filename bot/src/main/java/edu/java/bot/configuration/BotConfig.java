package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.bot.Bot;
import edu.java.bot.bot.BotImpl;
import edu.java.bot.processor.UserMessageProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Bean
    public Bot botImpl(
        @Value("${app.telegram-token}") String telegramToken,
        UserMessageProcessor userMessageProcessor
    ) {
        return new BotImpl(new TelegramBot(telegramToken), userMessageProcessor);
    }
}
