package cn.blogscn.fund.service.indexFund;

import cn.blogscn.fund.entity.indexFund.IndexFundRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IndexFundRecordService extends IService<IndexFundRecord> {

    Boolean updateAllAvgValue();

    Boolean updateDegree();

    BigDecimal calculateDegree(String code, LocalDate opendate);

    List<IndexFundRecord> queryFundRecordList(String code, LocalDate startDay, LocalDate endDay);

    boolean batchInsertOrUpdateJz(List<IndexFundRecord> indexFundRecordList);
}
