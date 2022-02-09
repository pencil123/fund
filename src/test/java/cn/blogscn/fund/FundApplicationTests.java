package cn.blogscn.fund;

import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.rabbitMq.SendMailJob;
import cn.blogscn.fund.rabbitMq.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.rabbitMq.bankuai.BkRecordUpdateJob;
import cn.blogscn.fund.rabbitMq.fund.FundDataInitJob;
import cn.blogscn.fund.rabbitMq.fund.FundRecordDataUpdateJob;
import cn.blogscn.fund.rabbitMq.gainian.GainianUpdateJob;
import cn.blogscn.fund.rabbitMq.gainian.GnRecordUpdateJob;
import cn.blogscn.fund.rabbitMq.index.IndexRecordDataUpdateJob;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
