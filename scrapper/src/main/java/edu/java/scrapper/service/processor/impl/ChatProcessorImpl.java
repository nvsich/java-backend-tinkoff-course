package edu.java.scrapper.service.processor.impl;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatIsRegisteredException;
import edu.java.scrapper.exception.ChatNotFoundException;
import edu.java.scrapper.repo.ChatRepo;
import edu.java.scrapper.service.processor.ChatProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatProcessorImpl implements ChatProcessor {

    private ChatRepo chatRepo;

    public void registerChat(Long id) {
        if (chatRepo.findByChatId(id).isPresent()) {
            throw new ChatIsRegisteredException("Chat is registered");
        }

        Chat newChat = new Chat();
        newChat.setChatId(id);

        chatRepo.save(newChat);
    }

    public void deleteChat(Long id) {
        if (chatRepo.findByChatId(id).isEmpty()) {
            throw new ChatNotFoundException("Chat not found");
        }

        chatRepo.deleteByChatId(id);
    }
}
