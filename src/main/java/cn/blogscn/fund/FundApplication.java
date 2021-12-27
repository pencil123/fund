package cn.blogscn.fund;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling //开启定时任务
@MapperScan("cn.blogscn.fund.mapper")
public class FundApplication {

    public static void main(String[] args) {
        //设置程序的时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(FundApplication.class, args);
    }

}
