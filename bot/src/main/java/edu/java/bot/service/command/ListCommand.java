package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.repo.ChatLinksRepo;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListCommand implements Command {

    private final ChatLinksRepo chatLinksRepo;

    private final ChatStateRepo chatStateRepo;

    private final LinkRepo linkRepo;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "View all tracking links";
    }

    @Override
    public MessageResponse handle(MessageRequest request) throws ChatNotFoundException {
        Long chatId = request.getChatId();

        ChatState chatState = chatStateRepo.findById(chatId).orElseThrow(ChatNotFoundException::new);

        chatState.setChatStatus(ChatStatus.WAITING_FOR_COMMAND);

        Set<Long> chatLinksIds = chatLinksRepo.findById(chatId);
        List<Link> links = linkRepo.findAllById(chatLinksIds);

        if (links.isEmpty()) {
            return new MessageResponse(chatId, "You are not tracking any links yet");
        }

        StringBuilder stringBuilder = new StringBuilder("Current tracking links:\n");
        for (Link link : links) {
            stringBuilder
                .append("id: ").append(link.getId()).append(". ")
                .append('[').append(link.getLinkDomain()).append("] ")
                .append(link.getUrl()).append('\n');
        }

        return new MessageResponse(chatId, stringBuilder.toString());
    }
}
