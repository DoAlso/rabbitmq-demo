package com.example.demorabbitmq;



import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;


@Component
public class RabbitMQClient {
    private static final String EXCHANGE = "itc-extern";
    private static final String ROUTING_KEY = "itc-extern-interface";
    private Channel channel;
    private Connection connection;
    private void getChannel() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("49.4.65.228");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE,"direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE,ROUTING_KEY);
        channel.basicQos(1);
        channel.basicConsume(queueName,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //TODO 上行数据的记录
                String result = new String(body);
                System.out.println("上行的消息是:"+result);
                try{

                }finally {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        });
    }

    @PostConstruct
    public void start(){
        try {
            getChannel();
        }catch (Exception e){

        }
    }

    @PreDestroy
    public void close(){
        if(channel != null){
            try {
                channel.close();
            }catch (Exception e){

            }
        }
        if(connection != null){
            try {
                connection.close();
            }catch (Exception e){

            }
        }
    }

}
