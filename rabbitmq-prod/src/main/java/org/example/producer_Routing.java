package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class producer_Routing {
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

        //创建队列 和 交换机

        /**
         * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
         * exchange 交换机名称
         * type 交互及类型（四种） 广播 定向 通配符 参数
         * durable 持久化
         * autoDelete 自动删除
         * internal 内部使用
         * arguments 参数
         * */

        String exchangeName = "test_TOPIC";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,false,null);

        //这里我们绑定设值两个队列
        String queue1Name = "T_queue";
        String queue2Name = "P_queue";

        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);

        /**
         * 队列名称
         * 交换机名称
         * 绑定规则 （注意如果是广播，则邦迪规则是空）
         * */
        //绑定队列和交换机

         channel.queueBind(queue1Name,exchangeName,"T.#");
         channel.queueBind(queue2Name,exchangeName,"T.*");


         //发送消息
        String body1 = "P是最imba的种族 人族打不了";
         channel.basicPublish(exchangeName,"T.123",null,body1.getBytes());

        String body2 = "T是最imba的种族 神族族打不了";
        channel.basicPublish(exchangeName,"T.123.123",null,body2.getBytes());
        channel.close();
        con.close();

     }

}