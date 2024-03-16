package edu.java.bot.entity;

import edu.java.bot.entity.enums.ChatStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_states")
public class ChatState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "chat_state_id")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_status")
    private ChatStatus chatStatus;

}
