package org.vcaml.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQListener {

    @RabbitListener(queues = "boot_queue_ttl")
    public void ListenerQueue(Message message,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                              Channel channel) throws IOException {
        try {
            System.out.println("收到消息为"+new String(message.getBody()));

            System.out.println("处理业务逻辑。。");

//            int i =3/0; //手动制造一个bug 让程序进入异常

            channel.basicAck(deliveryTag,true);

        }catch (Exception e){
            //拒绝签收
            //这里的第三个参数表示 如果为true 消息重回到queue broker会重新发送该消息给消费端
            System.out.println(" error 处理业务逻辑失败 消息打回 进入私信队列");
            channel.basicNack(deliveryTag,true,false);

            //这里拒收消息
            /**
             * deliveryTag 标识
             * requeue 是否打回原队列如果为false 则进入死信队列
             */
           // channel.basicReject(deliveryTag,false);
        }
    }

}
