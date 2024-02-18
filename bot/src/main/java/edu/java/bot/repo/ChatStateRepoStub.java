package edu.java.bot.repo;

import edu.java.bot.state.ChatState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
@AllArgsConstructor
public class ChatStateRepoStub implements ChatStateRepo {
    private HashMap<Long, ChatState> datasource;

    public ChatStateRepoStub() {
        datasource = new HashMap<>();
    }

    @Override
    public boolean containsChatState(Long chatId) {
        return datasource.containsKey(chatId);
    }

    @Override
    public ChatState findByChatId(Long chatId) {
        return datasource.get(chatId);
    }

    @Override
    public void setChatStateByChatId(Long chatId, ChatState chatState) {
        datasource.put(chatId, chatState);
    }
}
