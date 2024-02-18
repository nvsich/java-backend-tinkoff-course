package edu.java.bot.repo;

import edu.java.bot.state.ChatState;

public interface ChatStateRepo {

    boolean containsChatState(Long chatId);

    ChatState findByChatId(Long chatId);

    void setChatStateByChatId(Long chatId, ChatState chatState);
}
