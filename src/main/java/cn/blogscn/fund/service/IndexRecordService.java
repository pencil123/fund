package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.IndexRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IndexRecordService extends IService<IndexRecord> {
    Boolean updateAvgWeek();
    Boolean updateAvgMonth();
    Boolean updateAvgTwoWeek();
}
