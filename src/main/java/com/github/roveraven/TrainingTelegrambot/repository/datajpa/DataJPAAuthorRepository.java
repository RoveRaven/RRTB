package com.github.roveraven.TrainingTelegrambot.repository.datajpa;

import com.github.roveraven.TrainingTelegrambot.repository.AuthorRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("!jdbc")
@Repository
public interface DataJPAAuthorRepository extends AuthorRepository, JpaRepository<Author, Integer> {
}
