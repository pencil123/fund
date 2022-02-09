package cn.blogscn.fund.rabbitMq;

import cn.blogscn.fund.emuns.Process;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

    @Value("${rabbitmq.process.exchange}")
    private String exchange;

    @Value("${rabbitmq.process.routing}")
    private String routing;

    @Autowired
    private RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    public Boolean sendDirectMessage(Process process) {
        rabbitTemplate.convertAndSend(exchange, routing, process);
        return true;
    }

}
