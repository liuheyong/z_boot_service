package com.boot.service.rabbitmqsender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: LiuHeYong
 * @create: 2019-06-05
 * @description: RabbitMQSender
 **/
@Component
public class RabbitMQSender {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * @date: 2019/6/5
     * @description: 向RabbitMQQueue发送消息
     */
    public void send() {
        String context = "RabbitMQ Content" + new Date();
        this.rabbitTemplate.convertAndSend("RabbitMQQueueDestination", context);
    }

}
