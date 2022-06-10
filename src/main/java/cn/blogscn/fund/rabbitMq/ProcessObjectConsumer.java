package cn.blogscn.fund.rabbitMq;

import cn.blogscn.fund.emuns.Process;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ProcessObjectConsumer {

    private final static Logger logger = LoggerFactory.getLogger(ProcessObjectConsumer.class);
    @Autowired
    private Publisher publisher;


    @RabbitHandler
    @RabbitListener(queues = "${rabbitmq.process.queue}", containerFactory = "singleListenerContainer")
    public void consumerFundQueue(@Payload Process process,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        Boolean msgHandleStatus = false;
        switch (process) {
            case IndexList:
            case IndexRecord:
                break;
            case BankuaiList:
                break;
            case BankuaiRecord:
                break;
            case GainianList:
                break;
            case GainianRecord:
                break;
            case IndexFundList:
                break;
            case IndexFundRecord:
                break;
            case FundRecord:
                break;
            case SendMail:
                break;
        }
        try {
            if (msgHandleStatus) {
                channel.basicAck(deliveryTag, false);
            } else {
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "${rabbitmq.dlx.queue}", containerFactory = "singleListenerContainer")
    public void consumerDlxQueue(@Payload Process process,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        logger.info("消费消息：{}", process.toString());
        try {
            if (true) {
                channel.basicAck(deliveryTag, false);
            } else {
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
