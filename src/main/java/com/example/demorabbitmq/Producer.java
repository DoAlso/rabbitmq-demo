package com.example.demorabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

    public static void main(String[] args){
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("49.4.49.173");
            connection = factory.newConnection();
            channel = connection.createChannel();
            //定义一个交换机
            channel.exchangeDeclare("topicExchange", "topic",true);
            //定义一个消息队列
            channel.queueDeclare("topic.face",true,false,false,null);
            //绑定消息队列和交换机
            channel.queueBind("topic.face", "topicExchange", "topic.face");
            //发布消息
            channel.basicPublish("topicExchange","topic.face",null,"哈啊哈哈哈哈".getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                channel.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
