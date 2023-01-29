package com.example.crazyhorizintalscaling.rabbit;

import com.example.crazyhorizintalscaling.domain.CrazyTask;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class CrazyTaskSender {

    RabbitMessagingTemplate rabbitMessagingTemplate;

    public void sendCrazyTask(CrazyTask crazyTask) {
        rabbitMessagingTemplate.convertAndSend(CrazyTaskListener.CRAZY_TASKS_EXCHANGE, null, crazyTask);
    }
}
