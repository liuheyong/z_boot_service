package com.boot.service.activemqsender;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author: LiuHeYong
 * @create: 2019-05-28
 * @description:
 **/
public class QueueSender {
    public static void main(String[] args) {
        send();
    }

    private static void send() {
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://192.168.80.128:61616");
        Connection connection = null;
        Session session = null;
        MessageProducer mp = null;
        try {
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Message message = session.createTextMessage("queue测试数据");
            Destination destination = session.createQueue("myQueue");
            mp = session.createProducer(destination);
            mp.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }

                if (session != null) {
                    try {
                        session.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
                if (mp != null) {
                    try {
                        mp.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
