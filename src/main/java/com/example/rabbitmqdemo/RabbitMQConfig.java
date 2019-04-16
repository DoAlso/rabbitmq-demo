package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static final String EXCHANGE = "itc-extern";
    private static final String ROUTING_KEY = "itc-extern-interface";
    @Value("${host.luna}")
    private String host;


//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    /**
     * 定制化amqp模版      可根据需要定制多个
     * <p>
     * <p>
     * 此处为模版类定义 Jackson消息转换器
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     *
     * @return the amqp template
     */
//    @Bean
//    public AmqpTemplate amqpTemplate() {
//        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
//        // 使用jackson 消息转换器
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            String correlationId = message.getMessageProperties().getCorrelationId();
//            log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
//        });
//        // 消息确认，yml需要配置 publisher-confirms: true
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (ack) {
//                log.debug("消息发送到exchange成功,id: {}", correlationData.getId());
//            } else {
//                log.debug("消息发送到exchange失败,原因: {}", cause);
//            }
//        });
//        return rabbitTemplate;
//    }


    /**
     * 创建派工队列
     * @return
     */
    @Bean
    public Queue topicDispatchQueue() {
        return new Queue("topic.dispatch");
    }


    /**
     * 创建报警消息队列
     * @return
     */
    @Bean
    public Queue topicAlarmQueue() {
        return new Queue("topic.alarm");
    }


    /**
     * 创建交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }


    /**
     * 绑定交换机和相应的队列
     * @param topicDispatchQueue
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindingExchangeTopicDispatch(Queue topicDispatchQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicDispatchQueue).to(topicExchange).with("topic.dispatch");
    }


    /**
     * 绑定交换机和相应的队列
     * @param topicAlarmQueue
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindingExchangeTopicAlarm(Queue topicAlarmQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicAlarmQueue).to(topicExchange).with("topic.alarm");
    }


    @Bean
    public ConnectionFactory connectionFactory() throws Exception{
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.createConnection();
        Channel channel = connection.createChannel(true);
        channel.exchangeDeclare(EXCHANGE, "direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE, ROUTING_KEY);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) throws Exception{
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory());
        return factory;
    }

}
