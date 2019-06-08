package com.boot.service;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.boot.service.RedisCache.SpringBeanUtil;
import com.boot.service.activemqsender.BootQueueSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableCaching
@EnableAsync
@EnableRedisHttpSession
//剔除某些自动化配置
@SpringBootApplication
@EnableDubboConfiguration
//@MapperScan(basePackages = "com.boot.service.mapper")
public class ZBootServiceApplication {

    public static final Logger logger = LoggerFactory.getLogger(ZBootServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ZBootServiceApplication.class);
        //关闭命令行设置环境属性
        springApplication.setAddCommandLineProperties(false);
        ApplicationContext context = springApplication.run(args);
        //赋值ApplicationContext,以便随时手动获取bean
        SpringBeanUtil.setApplicationContext(context);
        logger.info("======获取到ApplicationContext======" + SpringBeanUtil.getApplicationContext());
        BootQueueSender activeMQSender = (BootQueueSender) context.getBean("bootQueueSender");
        activeMQSender.send();
        logger.info("======ActiveMQ往QueueDestination发送消息成功======");
        /*RabbitMQSender rabbitMQSender = (RabbitMQSender) context.getBean("rabbitMQSender");
        rabbitMQSender.send();
        logger.info("======RabbitMQ往RabbitMQQueueDestination发送消息成功======");*/
    }
}
