package edu.java.bot.service.command;

import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final List<Command> commands;

    private String helpText;

    @PostConstruct
    @Transactional
    protected void initializeHelpText() {
        helpText = commands.stream()
            .map(command -> command.command() + " -> " + command.description())
            .collect(Collectors.joining("\n"));
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "View commands list";
    }

    @Override
    public MessageResponse handle(MessageRequest request) {
        return new MessageResponse(request.getChatId(), this.helpText);
    }
}
