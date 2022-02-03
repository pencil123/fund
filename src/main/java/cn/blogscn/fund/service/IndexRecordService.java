package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.IndexRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDate;
import java.util.List;

public interface IndexRecordService extends IService<IndexRecord> {
    Boolean updateAvgWeek();
    Boolean updateAvgMonth();
    Boolean updateAvgTwoWeek();
    List<IndexRecord> queryRecordList(String code, LocalDate startDay,LocalDate endDay);
}