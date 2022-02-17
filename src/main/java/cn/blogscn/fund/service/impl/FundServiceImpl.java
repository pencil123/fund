package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.FundMapper;
import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.model.domain.FundRecord;
import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.service.FundService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FundServiceImpl extends ServiceImpl<FundMapper, Fund> implements FundService {

    @Autowired
    private FundRecordService fundRecordService;

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }


    @Override
    public Boolean updateStartAndEndDay() {
        List<Fund> fundList = list();
        QueryWrapper<FundRecord> fundRecordQueryWrapperAsc = new QueryWrapper<>();
        QueryWrapper<FundRecord> fundRecordQueryWrapperDesc = new QueryWrapper<>();
        for (Fund fund : fundList) {
            // startDay Asc
            fundRecordQueryWrapperAsc.select("opendate");
            fundRecordQueryWrapperAsc.orderByAsc("opendate");
            fundRecordQueryWrapperAsc.last("limit 1");
            fundRecordQueryWrapperAsc.eq("code", fund.getCode());
            FundRecord startDayOne = fundRecordService.getOne(fundRecordQueryWrapperAsc);
            fundRecordQueryWrapperAsc.clear();
            // endDay Desc
            fundRecordQueryWrapperDesc.select("opendate");
            fundRecordQueryWrapperDesc.orderByDesc("opendate");
            fundRecordQueryWrapperDesc.last("limit 1");
            fundRecordQueryWrapperDesc.eq("code", fund.getCode());
            FundRecord endDayOne = fundRecordService.getOne(fundRecordQueryWrapperDesc);
            fundRecordQueryWrapperDesc.clear();
            fund.setStartDay(startDayOne.getOpendate());
            fund.setEndDay(endDayOne.getOpendate());
            fund.setDegree(
                    fundRecordService.calculateDegree(fund.getCode(), endDayOne.getOpendate()));
            updateById(fund);
        }
        return true;
    }

    @Override
    public List<Fund> listByDegreeDesc() {
        QueryWrapper<Fund> fundQueryWrapper = new QueryWrapper<>();
        fundQueryWrapper.orderByDesc("degree");
        return list(fundQueryWrapper);
    }

    @Override
    public Boolean updateCount() {
        return baseMapper.updateCount();
    }
}
