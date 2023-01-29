package com.example.crazyhorizintalscaling.services;

import com.example.crazyhorizintalscaling.configs.RedisLock;
import com.example.crazyhorizintalscaling.domain.CrazyTask;
import com.example.crazyhorizintalscaling.rabbit.CrazyTaskSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CrazyTaskInitializerService {

//    public static final String SERVICE_ID = generateShortId();

    @Value("${server.port}")
    @NonFinal
    Integer port;

    RedisLock redisLock;

    CrazyTaskSender crazyTaskSender;

    private static final long ONE_MINUTE_IN_MILLIS = 1000 * 60;
    private static final String GENERATE_CRAZY_TASKS_KEY = "may:code:crazy:horizontal:scaling:generate:crazy:tasks";

    @Scheduled(cron = "0/15 * * * * *")
//    @Scheduled(fixedDelay = 8000L)
    public void generateCrazyTasks() {

        if (redisLock.acquireLock(ONE_MINUTE_IN_MILLIS, GENERATE_CRAZY_TASKS_KEY)) {

            log.info(Strings.repeat("-", 100));
            log.info(String.format("Service \"%s\" start generate tasks", port));

            for (int i = 0; i < 5; ++i) {
                crazyTaskSender.sendCrazyTask(
                        CrazyTask.builder()
                                .id(generateShortId())
                                .fromServerPort(port)
                                .build()
                );
            }

            log.info(String.format("Service \"%s\" end generate tasks", port));
            log.info(Strings.repeat("-", 100));

            redisLock.releaseLock(GENERATE_CRAZY_TASKS_KEY);
        }
    }

    private static String generateShortId() {
        return UUID.randomUUID().toString().substring(0, 4);
    }
}
