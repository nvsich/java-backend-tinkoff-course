package edu.java.bot.service.processor;

import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.service.command.Command;
import java.util.List;

public interface UserMessageProcessor {
    List<? extends Command> commands();

    MessageResponse process(MessageRequest request);
}
