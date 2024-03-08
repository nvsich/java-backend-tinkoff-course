package edu.java.bot.service.processor;

import edu.java.bot.dto.request.LinkUpdateRequest;

public interface LinkUpdateProcessor {
    void process(LinkUpdateRequest linkUpdateRequest);
}
