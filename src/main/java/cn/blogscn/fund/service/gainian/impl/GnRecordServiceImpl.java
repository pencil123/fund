package cn.blogscn.fund.service.gainian.impl;

import cn.blogscn.fund.entity.gainian.GnRecord;
import cn.blogscn.fund.mapper.gainian.GnRecordMapper;
import cn.blogscn.fund.service.gainian.GnRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GnRecordServiceImpl extends ServiceImpl<GnRecordMapper, GnRecord> implements
        GnRecordService {

    @Override
    public Boolean updateAllAvgValue() {
        QueryWrapper<GnRecord> gnRecordQueryWrapper = new QueryWrapper<>();
        gnRecordQueryWrapper.isNull("avg_week");
        gnRecordQueryWrapper.or().isNull("avg_two_week");
        gnRecordQueryWrapper.or().isNull("avg_month");
        gnRecordQueryWrapper.select("id", "code", "opendate");
        List<GnRecord> list = list(gnRecordQueryWrapper);
        for (GnRecord gnRecord : list) {
            gnRecord.setAvgTwoWeek(baseMapper.avgTwoWeek(
                    gnRecord.getOpendate().toString(), gnRecord.getCode()));
            gnRecord.setAvgWeek(baseMapper.avgWeek(
                    gnRecord.getOpendate().toString(), gnRecord.getCode()));
            gnRecord.setAvgMonth(baseMapper.avgMonth(
                    gnRecord.getOpendate().toString(), gnRecord.getCode()));
            updateById(gnRecord);
        }
        return null;
    }

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }

    @Override
    public List<GnRecord> queryRecordList(String code, LocalDate startDay, LocalDate endDay) {
        QueryWrapper<GnRecord> gnRecordQueryWrapper = new QueryWrapper<>();
        gnRecordQueryWrapper.eq("code", code);
        gnRecordQueryWrapper.between("opendate", startDay, endDay);
        gnRecordQueryWrapper.orderByDesc("opendate");
        return list(gnRecordQueryWrapper);
    }

    @Override
    public BigDecimal calculateDegree(String code, LocalDate opendate) {
        return baseMapper.calculateDegree(opendate.toString(), code);
    }
}
