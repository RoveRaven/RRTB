package com.github.roveraven.TrainingTelegrambot.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Telegram User entity.
 */

@Data
@ToString
@EqualsAndHashCode(exclude = "groupSubs")
@Entity
@Table(name = "tg_user")
public class TelegramUser {
    @Id
    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "active")
    private boolean active;
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<GroupSub> groupSubs;
}
