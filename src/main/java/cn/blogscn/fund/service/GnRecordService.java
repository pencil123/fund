package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.GnRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface GnRecordService extends IService<GnRecord> {

    Boolean updateAllAvgValue();

    Boolean updateDegree();

    List<GnRecord> queryRecordList(String code, LocalDate startDay, LocalDate endDay);

    BigDecimal calculateDegree(String code, LocalDate opendate);
}
