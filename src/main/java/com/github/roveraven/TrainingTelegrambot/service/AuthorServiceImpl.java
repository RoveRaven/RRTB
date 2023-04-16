package com.github.roveraven.TrainingTelegrambot.service;

import com.github.roveraven.TrainingTelegrambot.javarushclient.JavaRushPostClient;
import com.github.roveraven.TrainingTelegrambot.javarushclient.dto.PostInfo;
import com.github.roveraven.TrainingTelegrambot.repository.AuthorRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.Author;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.*;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final TelegramUserService telegramUserService;
    private final JavaRushPostClient javaRushPostClient;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, TelegramUserService telegramUserService, JavaRushPostClient javaRushPostClient) {
        this.authorRepository = authorRepository;
        this.telegramUserService = telegramUserService;
        this.javaRushPostClient = javaRushPostClient;
    }

    @Override
    public Author save(Long chatId, Integer authorId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        if(!telegramUser.isActive()) {
            throw  new NotFoundException("""
                                            Ooops... something goes wrong. 
                                            Probably, you didn't activate the bot. 
                                            Please, try to execute the \\\"start\" command
                                            """);
        } else {

            Optional<Author> authorFromDB = authorRepository.findById(authorId);
            if (authorFromDB.isPresent()) {
                TelegramUser user = new TelegramUser();
                user.setChatId(chatId);
                authorFromDB.get().getUsers2().add(user);
                return authorRepository.save(authorFromDB.get());
            } else {
                PostInfo post = tryGetPostByAuthor(chatId, authorId);
                Author author = new Author();
                author.setAuthorId(authorId);
                if(post!=null) {
                    author.setName(post.getAuthorInfo().getDisplayName());
                    author.setLastPostId(post.getId());
                }
                TelegramUser user = new TelegramUser();
                user.setChatId(chatId);
                author.setUsers2(Collections.singleton(user));
                return authorRepository.save(author);
            }
        }
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> findById(Integer authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    private PostInfo tryGetPostByAuthor(Long chatId, Integer authorId) {
        List<PostInfo> allPosts = javaRushPostClient.findAllPosts();
        List<PostInfo> postsOfAuthor = new ArrayList<>();
        for(PostInfo p : allPosts) {
            if (p.getAuthorInfo().getUserId().equals(authorId))
                postsOfAuthor.add(p);
        }
        if(postsOfAuthor.isEmpty()) {
            return null;
        } else return postsOfAuthor.stream()
                .max(Comparator.comparingInt(PostInfo::getId))
                .get();
    }
}
