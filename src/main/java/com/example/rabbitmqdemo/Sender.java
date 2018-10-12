package com.example.rabbitmqdemo;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    public void send() {
        String msgId = UUID.randomUUID().toString();
        //消息发送的前置处理器，将发送的消息包装上唯一的ID
        //当消息通过routingKey路由失败后可以通过该消息ID做出响应的处理
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setCorrelationId(msgId);
                // 设置消息持久化
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        };
        MessageProperties messageProperties = new MessageProperties();
        Message message = new Message("Hello,RabbitMQ".getBytes(),messageProperties);
        this.redisTemplate.opsForHash().put("mq.producer.retry.key", msgId, "");
        this.rabbitTemplate.convertAndSend("topicExchange","topic.dispatch", message,messagePostProcessor,new CorrelationData(msgId));
    }
}
