package edu.java.scrapper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLink {

    private Long chatId;

    private Long linkId;
}
