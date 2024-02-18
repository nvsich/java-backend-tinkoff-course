package edu.java.bot.utility;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import edu.java.bot.command.Command;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component @AllArgsConstructor
public class CommandsKeyboard {

    private final ApplicationContext applicationContext;

    public Keyboard getReplyKeyboardMarkup() {
        var beans = applicationContext.getBeansOfType(Command.class);
        var buttons = new ArrayList<String>();
        for (Command command : beans.values()) {
            buttons.add(command.command());
        }

        String[] buttonsArray = new String[buttons.size()];
        buttonsArray = buttons.toArray(buttonsArray);

        return new ReplyKeyboardMarkup(buttonsArray).resizeKeyboard(true);
    }
}
