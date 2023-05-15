package org.vcaml.text;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vcaml.rabbitmq.config.RabbitMQConfig;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     *
     * confirm
     */

    @Test
    public void testConfirm(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            //注意这是一个回调方法 等发送完成之后 再调用确认

            /**
             *
              * @param correlationData 配置信息
             * @param b adc 代表了exchange是否收到了信息
             * @param s
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("伟伟 confirm方法被执行了");

                if (b){
                    System.out.println("交换机接受成功"+s);
                }else{
                    System.out.println("交换机接受失败"+s);
                }
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.maru","boot mq hello aaa");
    }

    /**
     *
     * Return 模式
     * 监控exchange到queue过程 失败则调用returnCallback
     */

    @Test
    public void testReturn(){

        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback(){

            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("return 执行了");

            }
        });

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.maru","boot mq hello aaa");
    }

     /**
      * 删除队列
      * */
    @Test
    public void unBind(){

        amqpAdmin.deleteQueue("boot_queue_ttl");
    }
}
