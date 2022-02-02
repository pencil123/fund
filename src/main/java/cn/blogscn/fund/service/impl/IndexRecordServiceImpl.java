package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.IndexRecordMapper;
import cn.blogscn.fund.model.domain.IndexRecord;
import cn.blogscn.fund.service.IndexRecordService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class IndexRecordServiceImpl extends ServiceImpl<IndexRecordMapper,IndexRecord> implements IndexRecordService {

    @Override
    public Boolean updateAvgWeek() {
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        indexRecordQueryWrapper.isNull("avg_week");
        indexRecordQueryWrapper.select("id", "code", "opendate");
        List<IndexRecord> list = list(indexRecordQueryWrapper);
        for (IndexRecord indexRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(indexRecord.getOpendate().toString(), indexRecord.getCode());
            indexRecord.setAvgWeek(bigDecimal);
            updateById(indexRecord);
        }
        return true;
    }

    @Override
    public Boolean updateAvgMonth() {
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        indexRecordQueryWrapper.isNull("avg_month");
        indexRecordQueryWrapper.select("id", "code", "opendate");
        List<IndexRecord> list = list(indexRecordQueryWrapper);
        for (IndexRecord indexRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(indexRecord.getOpendate().toString(), indexRecord.getCode());
            indexRecord.setAvgMonth(bigDecimal);
            updateById(indexRecord);
        }
        return true;
    }

    @Override
    public Boolean updateAvgTwoWeek() {
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        indexRecordQueryWrapper.isNull("avg_two_week");
        indexRecordQueryWrapper.select("id", "code", "opendate");
        List<IndexRecord> list = list(indexRecordQueryWrapper);
        for (IndexRecord indexRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(indexRecord.getOpendate().toString(), indexRecord.getCode());
            indexRecord.setAvgTwoWeek(bigDecimal);
            updateById(indexRecord);
        }
        return true;
    }
}
