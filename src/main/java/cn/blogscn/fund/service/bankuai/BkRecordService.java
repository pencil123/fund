package cn.blogscn.fund.service.bankuai;

import cn.blogscn.fund.entity.bankuai.BkRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BkRecordService extends IService<BkRecord> {

    Boolean updateAllAvgValue();

    Boolean updateDegree();

    List<BkRecord> queryRecordList(String code, LocalDate startDay, LocalDate endDay);

    BigDecimal calculateDegree(String code, LocalDate opendate);
}
