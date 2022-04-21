package cn.blogscn.fund.service.indexFund.impl;

import cn.blogscn.fund.entity.indexFund.IndexFundRecord;
import cn.blogscn.fund.mapper.indexFund.IndexFundRecordMapper;
import cn.blogscn.fund.service.indexFund.IndexFundRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IndexFundRecordServiceImpl extends
        ServiceImpl<IndexFundRecordMapper, IndexFundRecord> implements IndexFundRecordService {

    @Override
    public Boolean updateAllAvgValue() {
        QueryWrapper<IndexFundRecord> indexFundRecordQueryWrapper = new QueryWrapper<>();
        indexFundRecordQueryWrapper.isNull("avg_week");
        indexFundRecordQueryWrapper.or().isNull("avg_two_week");
        indexFundRecordQueryWrapper.or().isNull("avg_month");
        indexFundRecordQueryWrapper.or().isNull("recent_week_price");
        indexFundRecordQueryWrapper.or().isNull("recent_two_week_price");
        indexFundRecordQueryWrapper.or().isNull("recent_month_price");
        indexFundRecordQueryWrapper.select("id", "code", "opendate");
        List<IndexFundRecord> list = list(indexFundRecordQueryWrapper);
        for (IndexFundRecord indexFundRecord : list) {
            indexFundRecord.setAvgTwoWeek(baseMapper.avgTwoWeek(
                    indexFundRecord.getOpendate().toString(), indexFundRecord.getCode()));
            indexFundRecord.setAvgWeek(baseMapper.avgWeek(
                    indexFundRecord.getOpendate().toString(), indexFundRecord.getCode()));
            indexFundRecord.setAvgMonth(baseMapper.avgMonth(
                    indexFundRecord.getOpendate().toString(), indexFundRecord.getCode()));
            indexFundRecord.setRecentMonthPrice(baseMapper
                    .recentMonthPrice(indexFundRecord.getOpendate().toString(),
                            indexFundRecord.getCode()));
            indexFundRecord.setRecentTwoWeekPrice(baseMapper
                    .recentTwoWeekPrice(indexFundRecord.getOpendate().toString(),
                            indexFundRecord.getCode()));
            indexFundRecord.setRecentWeekPrice(baseMapper
                    .recentWeekPrice(indexFundRecord.getOpendate().toString(),
                            indexFundRecord.getCode()));
            updateById(indexFundRecord);
        }
        return null;
    }

    @Override
    public Boolean updateDegree() {
        baseMapper.updatePriceRate();
        return baseMapper.updateDegree();
    }


    @Override
    public boolean batchInsertOrUpdateJz(List<IndexFundRecord> indexFundRecordList) {
        return baseMapper.batchInsertOrUpdateJz(indexFundRecordList);
    }


    @Override
    public List<IndexFundRecord> queryFundRecordList(String code, LocalDate startDay,
            LocalDate endDay) {
        QueryWrapper<IndexFundRecord> indexFundRecordQueryWrapper = new QueryWrapper<>();
        indexFundRecordQueryWrapper.eq("code", code);
        indexFundRecordQueryWrapper.between("opendate", startDay, endDay);
        indexFundRecordQueryWrapper.orderByDesc("opendate");
        return list(indexFundRecordQueryWrapper);
    }
}
