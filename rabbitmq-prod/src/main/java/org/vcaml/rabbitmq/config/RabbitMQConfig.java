package org.vcaml.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String QUEUE_NAME_TTL = "boot_queue_ttl";

    public static final String DEAD_EXCHANGE_NAME = "dead_topic_exchange";
    public static final String DEAD_QUEUE_NAME = "dead_queue";

    //正常交换机

    @Bean("bootExchange")
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }


    @Bean("bootQueue")
    public Queue bootQueue(){
        Map<String, Object> map = new HashMap<>();
        // 设置TTL
        map.put("x-message-ttl", 20000);
        // 设置死信的目的交换机
        map.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 设置死信交给目的交换机时的路由键
        map.put("x-dead-letter-routing-key", "boot.111");
        return QueueBuilder.durable(QUEUE_NAME_TTL).withArguments(map).build();
    }


    @Bean
    public Binding bindQueueExchange(@Qualifier("bootQueue") Queue queue,@Qualifier("bootExchange") Exchange exchange){
       return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

    //死信交换机
    @Bean("deadExchange")
    public Exchange deadExchange(){
        return ExchangeBuilder.topicExchange(DEAD_EXCHANGE_NAME).durable(true).build();
    }

    @Bean("deadQueue")
    public Queue deadQueue(){
        return QueueBuilder.durable(DEAD_QUEUE_NAME).build();
    }

    @Bean
    public Binding bindDeadQueueExchange(@Qualifier("deadQueue") Queue queue,@Qualifier("deadExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.*").noargs();
    }


}
