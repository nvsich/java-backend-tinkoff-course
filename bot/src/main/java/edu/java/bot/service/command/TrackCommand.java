package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.entity.enums.LinkDomain;
import edu.java.bot.entity.factory.LinkFactory;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.UnsupportedDomainException;
import edu.java.bot.repo.ChatLinksRepo;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TrackCommand implements Command {

    private final ChatStateRepo chatStateRepo;

    private final LinkRepo linkRepo;

    private final ChatLinksRepo chatLinksRepo;

    private final LinkFactory linkFactory;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking link";
    }

    @Override
    @Transactional
    public MessageResponse handle(MessageRequest request)
        throws ChatNotFoundException, URISyntaxException, ConnectException, UnsupportedDomainException {
        Long chatId = request.getChatId();

        ChatState chatState = chatStateRepo.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if (chatState.getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_TRACK)) {
            chatStateRepo.save(chatId, new ChatState(chatId, ChatStatus.WAITING_FOR_COMMAND));

            Link link = linkFactory.createLink(request.getText());

            if (link == null || link.getLinkDomain().equals(LinkDomain.NOT_SUPPORTED)) {
                throw new UnsupportedDomainException("This domain is not supported");
            }

            linkRepo.save(link);
            chatLinksRepo.save(chatId, link.getId());

            return new MessageResponse(chatId, "Link added to your tracking list");
        }

        chatStateRepo.save(chatId, new ChatState(chatId, ChatStatus.WAITING_FOR_LINK_TO_TRACK));
        return new MessageResponse(chatId, "Enter the link, that you want to track");
    }

    @Override
    public boolean supports(MessageRequest request) {
        Long chatId = request.getChatId();
        Optional<ChatState> chatState = chatStateRepo.findById(chatId);

        boolean isWaitingForLink =
            chatState.isPresent() && chatState.get().getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_TRACK);

        return Command.super.supports(request) || isWaitingForLink;
    }
}
