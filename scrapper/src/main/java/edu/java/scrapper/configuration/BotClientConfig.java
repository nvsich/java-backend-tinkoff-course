package edu.java.scrapper.configuration;

import edu.java.scrapper.api.client.BotClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BotClientConfig {

    @Value("${client.bot.baseUrl}")
    private String baseUrl;

    @Bean
    public BotClient botClient(WebClient.Builder webClientBuilder) {
        return new BotClient(webClientBuilder, baseUrl);
    }
}
