package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.BkRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BkRecordService extends IService<BkRecord> {
    Boolean updateAvgWeek();
    Boolean updateAvgMonth();
    Boolean updateAvgTwoWeek();
    List<BkRecord> queryRecordList(String code, LocalDate startDay,LocalDate endDay);
    BigDecimal calculateDegree(String code,LocalDate opendate);
}
