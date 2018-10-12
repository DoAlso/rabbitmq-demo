package com.example.demorabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Worker {
    static Connection connection = null;
    static Channel channel = null;
    public static void main(String[] args){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("49.4.5.95");
            connection = factory.newConnection();
            channel = connection.createChannel();
            //定义一个交换机
            channel.exchangeDeclare("direct", "direct",true);
            //定义一个消息队列
            String queueName = channel.queueDeclare().getQueue();
            //绑定消息队列和交换机
            channel.queueBind("direct.face", "direct", "direct.face");
            //消费消息
            channel.basicQos(1);
            channel.basicConsume("direct.face",new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //TODO 上行数据的记录
                    String result = new String(body);
                    System.out.println("上行的消息是:"+result);
                    channel.basicAck(envelope.getDeliveryTag(),false);

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
