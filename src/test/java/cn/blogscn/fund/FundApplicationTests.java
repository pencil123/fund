package cn.blogscn.fund;

import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.xxljob.SendMailJob;
import cn.blogscn.fund.xxljob.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.xxljob.bankuai.BkRecordUpdateJob;
import cn.blogscn.fund.xxljob.fund.FundDataInitJob;
import cn.blogscn.fund.xxljob.fund.FundRecordDataUpdateJob;
import cn.blogscn.fund.xxljob.gainian.GainianUpdateJob;
import cn.blogscn.fund.xxljob.gainian.GnRecordUpdateJob;
import cn.blogscn.fund.xxljob.index.IndexRecordDataUpdateJob;
import java.util.Date;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class FundApplicationTests {
    @Autowired
    private FundDataInitJob fundDataInitJob;
    @Autowired
    private FundRecordService fundRecordService;
    @Autowired
    private FundRecordDataUpdateJob fundRecordDataUpdateJob;
    @Autowired
    private BankuaiUpdateJob bankuaiUpdateJob;
    @Autowired
    private GainianUpdateJob gainianUpdateJob;
    @Autowired
    private GnRecordUpdateJob gnRecordDataUpdate;
    @Autowired
    private BkRecordUpdateJob bkRecordUpdateJob;
    @Autowired
    private IndexRecordDataUpdateJob indexRecordDataUpdateJob;

    @Test
    void fundDataInit() {
        fundDataInitJob.syncFundRecordData();
    }

    @Test
    void fundRecordDataUpdateTodayJob(){
        fundRecordDataUpdateJob.updateTodayData();
    }

    @Test
    void bankuaiListDataUpdate(){
        bankuaiUpdateJob.updateBankuaiData();
    }

    @Test
    void gainianListDataUpdate(){
        gainianUpdateJob.updateGainianData();
    }

    @Test
    void bkRecordDataUpdate()throws InterruptedException{
        bkRecordUpdateJob.updateBkRecords();
    }

    @Test
    void gnRecordDataUpdate()throws InterruptedException{
        gnRecordDataUpdate.updateGnRecords();
    }

    @Test
    void indicesDataUpdate(){
        indexRecordDataUpdateJob.indexRecordDataUpdateMain();
    }


    @Autowired
    private SendMailJob sendMailJob;
    @Test
    void sendMailJob() throws MessagingException {
        sendMailJob.sendMail();
    }
}
