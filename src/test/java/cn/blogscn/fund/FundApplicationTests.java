package cn.blogscn.fund;

import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.xxljob.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.xxljob.bankuai.BkRecordUpdateJob;
import cn.blogscn.fund.xxljob.fund.FundDataInitJob;
import cn.blogscn.fund.xxljob.fund.FundRecordDataUpdateJob;
import cn.blogscn.fund.xxljob.index.IndexRecordDataUpdateJob;
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
    void bkRecordDataUpdate()throws InterruptedException{
        bkRecordUpdateJob.updateBkRecords();
    }

    @Test
    void bkAvgValuesUpdate(){
        bkRecordUpdateJob.updateAvgValue();
    }

    @Test
    void indicesDataUpdate(){
        indexRecordDataUpdateJob.indexRecordDataUpdateMain();
    }

}
