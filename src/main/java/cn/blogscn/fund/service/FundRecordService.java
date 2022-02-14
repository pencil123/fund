package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.FundRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FundRecordService extends IService<FundRecord> {
    Boolean updateAllAvgValue();
    Boolean  updateDegree();
    IPage<FundRecord> queryFundRecordPage(String code,Long currentPage,Long pageSize);
    List<FundRecord> queryFundRecordList(String code, LocalDate startDay,LocalDate endDay);
    BigDecimal calculateDegree(String code,LocalDate opendate);
    Boolean batchInsert(List<FundRecord> fundRecordList);
}
