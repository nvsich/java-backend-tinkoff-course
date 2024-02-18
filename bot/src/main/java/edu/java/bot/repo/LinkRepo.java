package edu.java.bot.repo;

import java.util.List;

public interface LinkRepo {

    void saveLink(Long chatId, String url);

    void deleteLink(Long chatId, String url);

    List<String> getAll(Long chatId);
}
