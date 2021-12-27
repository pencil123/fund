package cn.blogscn.fund;

import cn.blogscn.fund.service.FundService;
import cn.blogscn.fund.service.job.SyncData;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private FundService fundService;

    @Test
    void test() {
        syncData.syncFundData();
    }

    @Test
    void updateAvgWeek(){
        fundService.updateAvgWeek("519983");
        fundService.updateAvgMonth("519983");
        fundService.updateAvg3month("519983");
    }

}
