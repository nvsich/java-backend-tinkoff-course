package edu.java.bot.processor;

import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;

public interface ChatStateAction {
    boolean execute(ChatStateRepo chatStateRepo, LinkRepo linkRepo, Long chatId, String link);
}
