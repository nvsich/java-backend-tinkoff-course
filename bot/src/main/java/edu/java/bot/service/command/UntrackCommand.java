package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.repo.ChatLinksRepo;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UntrackCommand implements Command {

    private final ChatStateRepo chatStateRepo;

    private final ChatLinksRepo chatLinksRepo;

    private final LinkRepo linkRepo;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking link";
    }

    @Override
    @Transactional
    public MessageResponse handle(MessageRequest request) throws ChatNotFoundException, IllegalArgumentException {
        Long chatId = request.getChatId();

        ChatState chatState = chatStateRepo.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if (chatState.getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK)) {
            chatStateRepo.save(chatId, new ChatState(chatId, ChatStatus.WAITING_FOR_COMMAND));

            Long linkId;
            try {
                linkId = Long.parseLong(request.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Id is not in correct format");
            }

            if (!chatLinksRepo.findById(chatId).contains(linkId)) {
                return new MessageResponse(chatId, "You don't track links with id " + linkId);
            }

            chatLinksRepo.deleteLinkId(chatId, linkId);
            linkRepo.deleteById(linkId);

            return new MessageResponse(chatId, "Link with id " + linkId + " is removed");
        }

        chatStateRepo.save(chatId, new ChatState(chatId, ChatStatus.WAITING_FOR_LINK_TO_UNTRACK));
        return new MessageResponse(chatId, "Enter the id of link, that you don't want to track anymore");
    }

    @Override
    public boolean supports(MessageRequest request) {
        Long chatId = request.getChatId();
        Optional<ChatState> chatState = chatStateRepo.findById(chatId);

        boolean isWaitingForLink =
            chatState.isPresent() && chatState.get().getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK);

        return Command.super.supports(request) || isWaitingForLink;
    }
}
