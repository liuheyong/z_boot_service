package com.boot.service.activemqsender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: LiuHeYong
 * @create: 2019-05-28
 * @description:
 **/
@Component("bootQueueSender")
public class BootQueueSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send() {
        jmsTemplate.send(session -> session.createTextMessage("boot的Queue的测试数据"));
    }
}
