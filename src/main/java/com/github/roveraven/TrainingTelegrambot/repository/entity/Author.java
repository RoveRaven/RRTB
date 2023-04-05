package com.github.roveraven.TrainingTelegrambot.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Entity
@Table(name = "author")
public class Author {
    @Id
    @Column(name = "author_id")
    private Integer author_id;
    @Column(name = "last_post_id")
    private Integer last_post_id;
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
    private List<TelegramUser> users2;
    public void addUser(TelegramUser telegramUser) {
        if (isNull(users2)) {
            users2 = new ArrayList<>();
        }
        users2.add(telegramUser);
    }
}
