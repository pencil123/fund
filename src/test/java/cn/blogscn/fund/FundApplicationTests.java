package cn.blogscn.fund;

import cn.blogscn.fund.service.RecordService;
import cn.blogscn.fund.service.job.SyncData;
import cn.blogscn.fund.service.job.UpdateData;
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
    private RecordService recordService;
    @Autowired
    private UpdateData updateData;

    @Test
    void test() {
        syncData.syncRecordData();
    }

    @Test
    void updateTodayData(){
        updateData.updateTodayData();
    }

    @Test
    void updateAvgWeek(){
        recordService.updateAvgWeek("519983");
        recordService.updateAvgMonth("519983");
        recordService.updateAvg3month("519983");
    }



}
