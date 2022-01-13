package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.RecordMapper;
import cn.blogscn.fund.model.domain.Record;
import cn.blogscn.fund.service.RecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;
import org.springframework.stereotype.Service;


@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    /**
     * 更新每周的均值
     */
    @Override
    public Boolean updateAvgWeek(String fundCode) {
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.isNull("avg_week");
        recordQueryWrapper.select("id", "fund_code", "fsrq");
        List<Record> list = list(recordQueryWrapper);
        for (Record record : list) {
            BigDecimal bigDecimal = baseMapper.avgWeek(record.getFsrq().toString(), record.getFundCode());
            record.setAvgWeek(bigDecimal);
            updateById(record);
        }
        return null;
    }


    /**
     * 更新每月均值
     */
    @Override
    public Boolean updateAvgMonth(String fundCode) {
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.isNull("avg_month");
        recordQueryWrapper.select("id", "fund_code", "fsrq");
        List<Record> list = list(recordQueryWrapper);
        for (Record record : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(record.getFsrq().toString(), record.getFundCode());
            record.setAvgMonth(bigDecimal);
            updateById(record);
        }
        return null;
    }

    /**
     * 更新三个月均值
     */

    @Override
    public Boolean updateAvg3month(String fundCode) {
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.isNull("avg3month");
        recordQueryWrapper.select("id", "fund_code", "fsrq");
        List<Record> list = list(recordQueryWrapper);
        for (Record record : list) {
            BigDecimal bigDecimal = baseMapper.avg3month(record.getFsrq().toString(), record.getFundCode());
            record.setAvg3month(bigDecimal);
            updateById(record);
        }
        return null;
    }
}
