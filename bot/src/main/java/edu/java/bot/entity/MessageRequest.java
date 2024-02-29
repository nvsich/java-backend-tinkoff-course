package edu.java.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageRequest {

    private Long chatId;

    private String text;

}
