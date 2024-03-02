package edu.java.scrapper.api.configuration;

import edu.java.scrapper.api.client.BotClient;
import edu.java.scrapper.api.client.impl.BotClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BotClientConfig {

    @Value("${client.bot.baseUrl}")
    private String baseUrl;

    public BotClient botClient(WebClient.Builder webClientBuilder) {
        return new BotClientImpl(webClientBuilder, baseUrl);
    }
}
