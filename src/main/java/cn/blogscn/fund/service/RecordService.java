package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Record;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RecordService extends IService<Record> {
    Boolean updateAvgWeek(String fundCode);
    Boolean updateAvgMonth(String fundCode);
    Boolean updateAvg3month(String fundCode);
}
