package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    public Author save(Long chatId, Integer authorID);
    Author save(Author author);
    Optional<Author> findById(Integer authorId);
    List<Author> findAll();
}
