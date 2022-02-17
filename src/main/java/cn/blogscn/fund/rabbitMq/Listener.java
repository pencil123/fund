package cn.blogscn.fund.rabbitMq;

import cn.blogscn.fund.emuns.Process;
import cn.blogscn.fund.rabbitMq.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.rabbitMq.bankuai.BkRecordUpdateJob;
import cn.blogscn.fund.rabbitMq.fund.FundRecordDataUpdateJob;
import cn.blogscn.fund.rabbitMq.gainian.GainianUpdateJob;
import cn.blogscn.fund.rabbitMq.gainian.GnRecordUpdateJob;
import cn.blogscn.fund.rabbitMq.index.IndexRecordDataUpdateJob;
import cn.blogscn.fund.rabbitMq.indexFund.IndexFundUpdateJob;
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
public class Listener {

    private final static Logger logger = LoggerFactory.getLogger(Listener.class);
    @Autowired
    private Publisher publisher;
    @Autowired
    private IndexRecordDataUpdateJob indexRecordDataUpdateJob;
    @Autowired
    private BankuaiUpdateJob bankuaiUpdateJob;
    @Autowired
    private BkRecordUpdateJob bkRecordUpdateJob;
    @Autowired
    private GainianUpdateJob gainianUpdateJob;
    @Autowired
    private GnRecordUpdateJob gnRecordUpdateJob;
    @Autowired
    private IndexFundUpdateJob indexFundUpdateJob;
    @Autowired
    private FundRecordDataUpdateJob fundRecordDataUpdateJob;
    @Autowired
    private SendMailJob sendMailJob;

    @RabbitHandler
    @RabbitListener(queues = "${rabbitmq.process.queue}", containerFactory = "singleListenerContainer")
    public void consumerFundQueue(@Payload Process process,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        Boolean msgHandleStatus = false;
        switch (process) {
            case IndexList:
            case IndexRecord:
                msgHandleStatus = indexRecordDataUpdateJob.indexRecordDataUpdateMain();
                if (msgHandleStatus) {
                    publisher.sendDirectMessage(Process.BankuaiList);
                }
                break;
            case BankuaiList:
                msgHandleStatus = bankuaiUpdateJob.updateBankuaiData();
                if (msgHandleStatus) {
                    publisher.sendDirectMessage(Process.BankuaiRecord);
                }
                break;
            case BankuaiRecord:
                msgHandleStatus = bkRecordUpdateJob.updateBkRecords();
                if (msgHandleStatus) {
                    publisher.sendDirectMessage(Process.GainianList);
                }
                break;
            case GainianList:
                msgHandleStatus = gainianUpdateJob.updateGainianData();
                if (msgHandleStatus) {
                    publisher.sendDirectMessage(Process.GainianRecord);
                }
                break;
            case GainianRecord:
                msgHandleStatus = gnRecordUpdateJob.updateGnRecords();
                if (msgHandleStatus) {
                    publisher.sendDirectMessage(Process.FundList);
                }
                break;
            case FundList:
                msgHandleStatus = indexFundUpdateJob.updateIndexFund();
                if (msgHandleStatus) {
                    publisher.sendDirectMessage(Process.FundRecord);
                }
            case FundRecord:
                msgHandleStatus = fundRecordDataUpdateJob.updateTodayData();
                if (msgHandleStatus) {
                    publisher.sendDirectMessage(Process.SendMail);
                }
                break;
            case SendMail:
                msgHandleStatus = sendMailJob.sendMail();
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
