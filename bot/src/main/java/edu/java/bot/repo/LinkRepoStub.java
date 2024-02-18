package edu.java.bot.repo;

import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LinkRepoStub implements LinkRepo {

    private HashMap<Long, ArrayList<String>> datasource;

    public LinkRepoStub() {
        this.datasource = new HashMap<>();
    }

    @Override
    public void saveLink(Long chatId, String link) {
        datasource.computeIfAbsent(chatId, k -> new ArrayList<>());
        datasource.get(chatId).add(link);
    }

    @Override
    public void deleteLink(Long chatId, String link) {
        datasource.get(chatId).remove(link);
    }

    @Override
    public List<String> getAll(Long chatId) {
        return datasource.get(chatId);
    }
}
