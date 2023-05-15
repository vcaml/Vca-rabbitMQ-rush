package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class consumer_work2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 设定工厂参数
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.88.151");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/itcast");
        connectionFactory.setUsername("heima");
        connectionFactory.setPassword("heima");

        //建立连接
        Connection con = connectionFactory.newConnection();

        //创建channel
        Channel channel = con.createChannel();


        String queue2Name = "T_queue";

        //创建队列

        /**
         * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
         *
         * queue 队列名称
         * durable 是否持久化， 当mq重启之后 是否还在
         * exclusive 是否独占：只能有一个消费者监听这个队列。 当con关闭时，是否删除队列
         * autoDelete 当这个队列没有消费者的时候 自动删除
         * */

        //如果没有这个队列 会自动创建，如果有就不会再创建了
    //    channel.queueDeclare("hello_world",true,false,false,null);

        Consumer consumer = new DefaultConsumer(channel){

            /***
             * 回调方法 当收到消息后 会自动执行该方法
             * consumerTag 标识
             * envelope 获取信息 交换机，路由key
             * properties 配置的信息
             * body 数据
             */

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("tag标识:"+consumerTag);
                System.out.println("交换机:"+envelope.getExchange()+" "+"路由key"+envelope.getRoutingKey());
//                System.out.println("3:"+properties);
                System.out.println("数据:"+ new String(body));

            }
        };

        /**
         * basicConsume(String queue, boolean autoAck, Consumer callback)
         * queue 队列名称
         * autoAck 是否自动确认 消费者收到之后自动给mq说一声 我收到了 作为确认
         * callback 回调对象 它会监听一些方法
         * */

        channel.basicConsume(queue2Name,true,consumer);


//        消费者不要关闭资源释放资源
//        channel.close();
//        con.close();


    }
}