package edu.java.bot.utility;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LinkParser {
    public static boolean isReachableUrl(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getStatusCode().is2xxSuccessful();
    }
}
