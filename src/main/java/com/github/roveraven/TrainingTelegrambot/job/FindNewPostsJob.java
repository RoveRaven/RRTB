package com.github.roveraven.TrainingTelegrambot.job;

import com.github.roveraven.TrainingTelegrambot.service.FindNewPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

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
        Instant start = Instant.now();
        log.info("Find new posts job started.");
        findNewPostService.findNewPosts();
        Instant end = Instant.now();

        log.info("Find new posts job finished. Took milliseconds: {}",
                end.toEpochMilli()-start.toEpochMilli());
    }
}
