package com.example.rabbitmqdemo;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
            value = @Queue(value = "topic.dispatch",durable = "true"), exchange = @Exchange(value = "topicExchange",type = ExchangeTypes.TOPIC),
            key = "topic.dispatch"))
    public void process(String msg) {
        System.out.println("receive message: "+ msg);
    }
}
