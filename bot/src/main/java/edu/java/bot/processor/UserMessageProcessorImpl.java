package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import edu.java.bot.state.ChatState;
import edu.java.bot.utility.CommandsKeyboard;
import java.util.HashMap;
import org.springframework.context.ApplicationContext;
import java.util.List;

public class UserMessageProcessorImpl implements UserMessageProcessor {
    private final List<Command> commands;

    private final ChatStateRepo chatStateRepo;

    private final LinkRepo linkRepo;

    private final CommandsKeyboard keyboard;

    private final HashMap<ChatState, ChatStateAction> commandExecutor;

    public UserMessageProcessorImpl(
        List<Command> commands, ChatStateRepo chatStateRepo, LinkRepo linkRepo, ApplicationContext applicationContext
    ) {
        this.commands = commands;
        this.chatStateRepo = chatStateRepo;
        this.linkRepo = linkRepo;
        this.keyboard = new CommandsKeyboard(applicationContext);

        this.commandExecutor = new HashMap<>();
        commandExecutor.put(ChatState.WAITING_FOR_LINK_TO_TRACK, new StartTrackingLink());
        commandExecutor.put(ChatState.WAITING_FOR_LINK_TO_UNTRACK, new StopTrackingLink());
    }

    @Override public List<? extends Command> commands() {
        return commands;
    }

    @Override public SendMessage process(Update update) {
        Long chatId = update.message().chat().id();

        if (!chatStateRepo.containsChatState(chatId)) {
            chatStateRepo.setChatStateByChatId(chatId, ChatState.WAITING_FOR_START);
        }

        for (var command : commands()) {
            if (command.supports(update, chatStateRepo.findByChatId(chatId))) {

                chatStateRepo.setChatStateByChatId(chatId, command.nextChatState());

                return command.handle(update).replyMarkup(this.keyboard.getReplyKeyboardMarkup());
            }
        }

        ChatStateAction chatStateAction = commandExecutor.get(chatStateRepo.findByChatId(chatId));

        if (chatStateAction != null) {
            boolean isCorrectlyExecuted =
                chatStateAction.execute(chatStateRepo, linkRepo, chatId, update.message().text());

            if (isCorrectlyExecuted) {
                return new SendMessage(update.message().chat().id(), "Job is done.");
            }
        }

        String replyText =
            chatStateRepo.findByChatId(chatId).equals(ChatState.WAITING_FOR_START) ? "/start to start using."
                : "Unknown command.\nUse /help for commands list.";

        return new SendMessage(update.message().chat().id(),
            replyText
        ).replyMarkup(this.keyboard.getReplyKeyboardMarkup());
    }
}
