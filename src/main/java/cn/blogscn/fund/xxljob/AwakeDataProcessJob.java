package cn.blogscn.fund.xxljob;

import cn.blogscn.fund.emuns.Process;
import cn.blogscn.fund.rabbitMq.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AwakeDataProcessJob {

    @Autowired
    private Publisher publisher;

    @Scheduled(cron = "0 1 23 ? * MON-FRI")
    public void sendMegToMQ() {
        publisher.sendDirectMessage(Process.IndexList);
    }
}
