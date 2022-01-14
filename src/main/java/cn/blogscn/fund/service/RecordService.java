package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Record;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDate;
import java.util.List;

public interface RecordService extends IService<Record> {
    Boolean updateAvgWeek(String fundCode);
    Boolean updateAvgMonth(String fundCode);
    Boolean updateAvg3month(String fundCode);

    IPage<Record> queryRecordPage(String fundCode,Long currentPage,Long pageSize);
    List<Record> queryRecordList(String fundCode, LocalDate startDay,LocalDate endDay);
}
