package edu.java.bot.processor;

import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import edu.java.bot.state.ChatState;
import edu.java.bot.utility.LinkParser;

public class StartTrackingLink implements ChatStateAction {
    @Override
    public boolean execute(ChatStateRepo chatStateRepo, LinkRepo linkRepo, Long chatId, String link) {
        chatStateRepo.setChatStateByChatId(chatId, ChatState.WAITING_FOR_COMMAND);

        try {
            if (LinkParser.isReachableUrl(link)) {
                linkRepo.saveLink(chatId, link);
                return true;
            }

            return false;

        } catch (Exception ex) {
            return false;
        }
    }
}
