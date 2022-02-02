package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.FundRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDate;
import java.util.List;

public interface FundRecordService extends IService<FundRecord> {
    Boolean updateAvgWeek();
    Boolean updateAvgMonth();
    Boolean updateAvgTwoWeek();

    IPage<FundRecord> queryFundRecordPage(String fundCode,Long currentPage,Long pageSize);
    List<FundRecord> queryFundRecordList(String fundCode, LocalDate startDay,LocalDate endDay);
}
