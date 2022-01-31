package cn.blogscn.fund;

import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.xxljob.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.xxljob.fund.SyncData;
import cn.blogscn.fund.xxljob.fund.UpdateData;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class FundApplicationTests {
    @Autowired
    private SyncData syncData;
    @Autowired
    private FundRecordService fundRecordService;
    @Autowired
    private UpdateData updateData;
    @Autowired
    private BankuaiUpdateJob bankuaiUpdateJob;

    @Test
    void test() {
        syncData.syncFundRecordData();
    }

    @Test
    void updateTodayData(){
        updateData.updateTodayData();
    }

    @Test
    void updateAvgWeek(){
        fundRecordService.updateAvgWeek("519983");
        fundRecordService.updateAvgMonth("519983");
        fundRecordService.updateAvg3month("519983");
    }

    @Test
    void bankuaiListDataUpdate(){
        bankuaiUpdateJob.updateBankuaiData();
    }


}
