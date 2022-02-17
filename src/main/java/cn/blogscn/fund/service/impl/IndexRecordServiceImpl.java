package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.IndexRecordMapper;
import cn.blogscn.fund.model.domain.IndexRecord;
import cn.blogscn.fund.service.IndexRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IndexRecordServiceImpl extends ServiceImpl<IndexRecordMapper, IndexRecord> implements
        IndexRecordService {

    @Override
    public Boolean updateAllAvgValue() {
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        indexRecordQueryWrapper.isNull("avg_week");
        indexRecordQueryWrapper.or().isNull("avg_two_week");
        indexRecordQueryWrapper.or().isNull("avg_month");
        indexRecordQueryWrapper.select("id", "code", "opendate");
        List<IndexRecord> list = list(indexRecordQueryWrapper);
        for (IndexRecord indexRecord : list) {
            indexRecord.setAvgTwoWeek(baseMapper.avgTwoWeek(
                    indexRecord.getOpendate().toString(), indexRecord.getCode()));
            indexRecord.setAvgWeek(baseMapper.avgWeek(
                    indexRecord.getOpendate().toString(), indexRecord.getCode()));
            indexRecord.setAvgMonth(baseMapper.avgMonth(
                    indexRecord.getOpendate().toString(), indexRecord.getCode()));
            updateById(indexRecord);
        }
        return null;
    }

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }

    @Override
    public List<IndexRecord> queryRecordList(String code, LocalDate startDay, LocalDate endDay) {
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        indexRecordQueryWrapper.eq("code", code);
        indexRecordQueryWrapper.between("opendate", startDay, endDay);
        indexRecordQueryWrapper.orderByDesc("opendate");
        return list(indexRecordQueryWrapper);
    }

    @Override
    public BigDecimal calculateDegree(String code, LocalDate opendate) {
        return baseMapper.calculateDegree(opendate.toString(), code);
    }
}
