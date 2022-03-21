package cn.blogscn.fund.service.bankuai.impl;

import cn.blogscn.fund.entity.bankuai.BkRecord;
import cn.blogscn.fund.mapper.bankuai.BkRecordMapper;
import cn.blogscn.fund.service.bankuai.BkRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BkRecordServiceImpl extends ServiceImpl<BkRecordMapper, BkRecord> implements
        BkRecordService {

    @Override
    public Boolean updateAllAvgValue() {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.isNull("avg_week");
        bkRecordQueryWrapper.or().isNull("avg_two_week");
        bkRecordQueryWrapper.or().isNull("avg_month");
        bkRecordQueryWrapper.select("id", "code", "opendate");
        List<BkRecord> list = list(bkRecordQueryWrapper);
        for (BkRecord bkRecord : list) {
            bkRecord.setAvgTwoWeek(baseMapper.avgTwoWeek(
                    bkRecord.getOpendate().toString(), bkRecord.getCode()));
            bkRecord.setAvgWeek(baseMapper.avgWeek(
                    bkRecord.getOpendate().toString(), bkRecord.getCode()));
            bkRecord.setAvgMonth(baseMapper.avgMonth(
                    bkRecord.getOpendate().toString(), bkRecord.getCode()));
            updateById(bkRecord);
        }
        return null;
    }

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }

    @Override
    public List<BkRecord> queryRecordList(String code, LocalDate startDay, LocalDate endDay) {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.eq("code", code);
        bkRecordQueryWrapper.between("opendate", startDay, endDay);
        bkRecordQueryWrapper.orderByDesc("opendate");
        return list(bkRecordQueryWrapper);
    }

    @Override
    public BigDecimal calculateDegree(String code, LocalDate opendate) {
        return baseMapper.calculateDegree(opendate.toString(), code);
    }
}
