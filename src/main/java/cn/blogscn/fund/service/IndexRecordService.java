package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.IndexRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IndexRecordService extends IService<IndexRecord> {
    Boolean updateAllAvgValue();
    Boolean  updateDegree();
    List<IndexRecord> queryRecordList(String code, LocalDate startDay,LocalDate endDay);
    BigDecimal calculateDegree(String code,LocalDate opendate);
}
