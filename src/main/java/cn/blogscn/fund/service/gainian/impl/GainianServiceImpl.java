package cn.blogscn.fund.service.gainian.impl;

import cn.blogscn.fund.entity.gainian.Gainian;
import cn.blogscn.fund.entity.gainian.GnRecord;
import cn.blogscn.fund.mapper.gainian.GainianMapper;
import cn.blogscn.fund.service.gainian.GainianService;
import cn.blogscn.fund.service.gainian.GnRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GainianServiceImpl extends ServiceImpl<GainianMapper, Gainian> implements
        GainianService {

    @Autowired
    private GnRecordService gnRecordService;

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }

    @Override
    public Boolean updateStartAndEndDay() {
        List<Gainian> gainains = list();
        QueryWrapper<GnRecord> gnRecordQueryWrapperAsc = new QueryWrapper<>();
        QueryWrapper<GnRecord> gnRecordQueryWrapperDesc = new QueryWrapper<>();
        for (Gainian gainian : gainains) {
            // startDay Asc
            gnRecordQueryWrapperAsc.select("opendate");
            gnRecordQueryWrapperAsc.orderByAsc("opendate");
            gnRecordQueryWrapperAsc.last("limit 1");
            gnRecordQueryWrapperAsc.eq("code", gainian.getCode());
            GnRecord startDayOne = gnRecordService.getOne(gnRecordQueryWrapperAsc);
            gnRecordQueryWrapperAsc.clear();
            // endDay Desc
            gnRecordQueryWrapperDesc.select("opendate");
            gnRecordQueryWrapperDesc.orderByDesc("opendate");
            gnRecordQueryWrapperDesc.last("limit 1");
            gnRecordQueryWrapperDesc.eq("code", gainian.getCode());
            GnRecord endDayOne = gnRecordService.getOne(gnRecordQueryWrapperDesc);
            gnRecordQueryWrapperDesc.clear();
            gainian.setStartDay(startDayOne.getOpendate());
            gainian.setEndDay(endDayOne.getOpendate());
            gainian.setDegree(
                    gnRecordService.calculateDegree(gainian.getCode(), endDayOne.getOpendate()));
            updateById(gainian);
        }
        return true;
    }

    @Override
    public List<Gainian> listByDegreeDesc() {
        QueryWrapper<Gainian> gainianQueryWrapper = new QueryWrapper<>();
        gainianQueryWrapper.orderByDesc("degree");
        return list(gainianQueryWrapper);
    }
}
