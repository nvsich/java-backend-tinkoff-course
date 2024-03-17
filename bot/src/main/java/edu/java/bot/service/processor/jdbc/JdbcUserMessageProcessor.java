package edu.java.bot.service.processor.jdbc;

import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.service.command.Command;
import edu.java.bot.service.processor.UserMessageProcessor;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcUserMessageProcessor implements UserMessageProcessor {

    private List<Command> commands;

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public MessageResponse process(MessageRequest request) {
        for (Command command : commands) {
            if (command.supports(request)) {
                try {
                    return command.handle(request);
                } catch (ChatNotFoundException e) {
                    return new MessageResponse(request.getChatId(), "Use /start to start the bot");
                } catch (Exception e) {
                    return new MessageResponse(request.getChatId(), e.getMessage());
                }
            }
        }

        return new MessageResponse(request.getChatId(), "Unsupported command.\n/help for list of commands.");
    }
}
