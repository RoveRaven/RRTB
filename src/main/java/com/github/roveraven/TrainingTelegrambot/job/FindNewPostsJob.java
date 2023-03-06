package com.github.roveraven.TrainingTelegrambot.job;

import com.github.roveraven.TrainingTelegrambot.service.FindNewPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Job for finding new posts.
 */
@Slf4j
@Component
public class FindNewPostsJob {
    private final FindNewPostService findNewPostService;

    @Autowired
    public FindNewPostsJob(FindNewPostService findNewPostService) {
        this.findNewPostService = findNewPostService;
    }

    @Scheduled(fixedRateString ="${bot.recountNewPostFixedRate}")
    public void findNewPosts() {
        LocalDateTime start = LocalDateTime.now();
        log.info("Find new posts job started.");
        findNewPostService.findNewPosts();
        LocalDateTime end = LocalDateTime.now();
        log.info("Find new posts job finished. Took seconds: {}",
                end.toEpochSecond(ZoneOffset.UTC)- start.toEpochSecond(ZoneOffset.UTC));
    }
}
