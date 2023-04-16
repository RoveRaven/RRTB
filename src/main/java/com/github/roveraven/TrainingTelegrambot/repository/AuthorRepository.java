package com.github.roveraven.TrainingTelegrambot.repository;

import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(Integer id);
    Author save(Author author);
    List<Author> findAll();
}
