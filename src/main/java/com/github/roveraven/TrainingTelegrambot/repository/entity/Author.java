package com.github.roveraven.TrainingTelegrambot.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@Data
@Entity
@Table(name = "author")
public class Author {
    @Id
    @Column(name = "author_id")
    private Integer authorId;
    @Column(name = "last_post_id")
    private Integer lastPostId;
    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "author_x_user",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<TelegramUser> users2;
    public void addUser(TelegramUser telegramUser) {
        if (isNull(users2)) {
            users2 = new HashSet<>();
        }
        users2.add(telegramUser);
    }
}
