package edu.java.bot.processor;

import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import edu.java.bot.state.ChatState;

public class StopTrackingLink implements ChatStateAction {
    @Override
    public boolean execute(ChatStateRepo chatStateRepo, LinkRepo linkRepo, Long chatId, String link) {
        chatStateRepo.setChatStateByChatId(chatId, ChatState.WAITING_FOR_COMMAND);

        try {
            if (linkRepo.getAll(chatId).contains(link)) {
                linkRepo.deleteLink(chatId, link);
                return true;
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }
}
