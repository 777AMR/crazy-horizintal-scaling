package com.example.crazyhorizintalscaling.rabbit;

import com.example.crazyhorizintalscaling.domain.CrazyTask;
import com.example.crazyhorizintalscaling.services.CrazyTaskInitializerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class CrazyTaskListener {

    @Value("${server.port}")
    @NonFinal
    Integer port;

    private static final String CRAZY_TASKS_QUEUE = "may.code.crazy.tasks.queue";
    public static final String CRAZY_TASKS_EXCHANGE = "may.code.crazy.tasks.exchange";

    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = CRAZY_TASKS_QUEUE, durable = Exchange.TRUE, autoDelete = Exchange.TRUE),
                    exchange = @Exchange(value = CRAZY_TASKS_EXCHANGE, autoDelete= Exchange.TRUE)
            )
    )
    public void handleCrazyTask(CrazyTask crazyTask) {

        Thread.sleep(15_000);

        log.info(
                String.format(
                        "Service \"%s\" end process task \"%s\" from service \"%s\"",
                        port,
                        crazyTask.getId(),
                        crazyTask.getFromServerPort()
                )
        );
    }
}
