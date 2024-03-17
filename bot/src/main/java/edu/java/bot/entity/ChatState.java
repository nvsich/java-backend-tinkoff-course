package edu.java.bot.entity;

import edu.java.bot.entity.enums.ChatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatState {

    private Long chatId;

    private ChatStatus chatStatus;

}
