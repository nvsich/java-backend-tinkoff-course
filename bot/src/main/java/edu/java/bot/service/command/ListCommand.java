package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.IncorrectRequestParamsException;
import edu.java.bot.repo.ChatStateRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListCommand implements Command {
    private static final String NOT_TRACKING_TEXT = "You are not tracking any links yet";
    private final ChatStateRepo chatStateRepo;
    private ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "View all tracking links";
    }

    @Override
    public MessageResponse handle(MessageRequest request) {
        Long chatId = request.getChatId();

        ChatState chatState = chatStateRepo.findByChatId(chatId).orElseThrow(ChatNotFoundException::new);

        chatState.setChatStatus(ChatStatus.WAITING_FOR_COMMAND);

        try {
            var listLinkResponseFromClient = scrapperClient.getAllLinksForChat(chatId);

            if (listLinkResponseFromClient == null) {
                return new MessageResponse(chatId, NOT_TRACKING_TEXT);
            }

            var listLinkResponse = listLinkResponseFromClient.getLinks();

            if (listLinkResponse.isEmpty()) {
                return new MessageResponse(chatId, NOT_TRACKING_TEXT);
            }

            StringBuilder stringBuilder = new StringBuilder("Current tracking links:\n");
            for (LinkResponse linkResponse : listLinkResponse) {
                String url = linkResponse.getUrl().toString();
                stringBuilder.append(url).append('\n');
            }

            return new MessageResponse(chatId, stringBuilder.toString());

        } catch (IncorrectRequestParamsException e) {
            return new MessageResponse(chatId, e.getMessage());
        }
    }
}
