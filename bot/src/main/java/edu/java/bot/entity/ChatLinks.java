package edu.java.bot.entity;

import java.util.Set;
import lombok.Data;

@Data
public class ChatLinks {
    private Long chatId;

    private Set<Long> linkIds;
}
