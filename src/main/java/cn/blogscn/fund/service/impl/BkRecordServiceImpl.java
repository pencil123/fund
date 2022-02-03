package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.BkRecordMapper;
import cn.blogscn.fund.model.domain.BkRecord;
import cn.blogscn.fund.service.BkRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BkRecordServiceImpl extends ServiceImpl<BkRecordMapper, BkRecord> implements BkRecordService {

    @Override
    public Boolean updateAvgWeek() {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.isNull("avg_week");
        bkRecordQueryWrapper.select("id", "code", "opendate");
        List<BkRecord> list = list(bkRecordQueryWrapper);
        for (BkRecord bkRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgWeek(bkRecord.getOpendate().toString(), bkRecord.getCode());
            bkRecord.setAvgWeek(bigDecimal);
            updateById(bkRecord);
        }
        return true;
    }

    @Override
    public Boolean updateAvgMonth() {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.isNull("avg_month");
        bkRecordQueryWrapper.select("id", "code", "opendate");
        List<BkRecord> list = list(bkRecordQueryWrapper);
        for (BkRecord bkRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(bkRecord.getOpendate().toString(), bkRecord.getCode());
            bkRecord.setAvgMonth(bigDecimal);
            updateById(bkRecord);
        }
        return true;
    }

    @Override
    public Boolean updateAvgTwoWeek() {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.isNull("avg_two_week");
        bkRecordQueryWrapper.select("id", "code", "opendate");
        List<BkRecord> list = list(bkRecordQueryWrapper);
        for (BkRecord bkRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgTwoWeek(bkRecord.getOpendate().toString(), bkRecord.getCode());
            bkRecord.setAvgTwoWeek(bigDecimal);
            updateById(bkRecord);
        }
        return true;
    }

    @Override
    public List<BkRecord> queryRecordList(String code, LocalDate startDay, LocalDate endDay) {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.eq("code",code);
        bkRecordQueryWrapper.between("opendate",startDay,endDay);
        bkRecordQueryWrapper.orderByDesc("opendate");
        return list(bkRecordQueryWrapper);
    }


}
