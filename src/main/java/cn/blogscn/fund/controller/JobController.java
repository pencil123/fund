package cn.blogscn.fund.controller;

import cn.blogscn.fund.emuns.Process;
import cn.blogscn.fund.rabbitMq.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Autowired
    private Publisher publisher;

    @GetMapping("/rabbitmqTest")
    public String rabbitmqTest() {
        publisher.sendDirectMessage(Process.IndexList);
        return "success";
    }
}
