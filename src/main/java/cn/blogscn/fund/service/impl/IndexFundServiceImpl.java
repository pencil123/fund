package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.IndexFundMapper;
import cn.blogscn.fund.model.domain.FundRecord;
import cn.blogscn.fund.model.domain.IndexFund;
import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.service.IndexFundService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexFundServiceImpl extends ServiceImpl<IndexFundMapper, IndexFund> implements
        IndexFundService {

    @Autowired
    private IndexFundService indexFundService;
    @Autowired
    private FundRecordService fundRecordService;

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }

    @Override
    public Boolean updateStartAndEndDay() {
        List<IndexFund> indexFundList = list();
        QueryWrapper<FundRecord> fundRecordQueryWrapperAsc = new QueryWrapper<>();
        QueryWrapper<FundRecord> fundRecordQueryWrapperDesc = new QueryWrapper<>();
        for (IndexFund indexFund : indexFundList) {
            // startDay Asc
            fundRecordQueryWrapperAsc.select("opendate");
            fundRecordQueryWrapperAsc.orderByAsc("opendate");
            fundRecordQueryWrapperAsc.last("limit 1");
            fundRecordQueryWrapperAsc.eq("code", indexFund.getCode());
            FundRecord startDayOne = fundRecordService.getOne(fundRecordQueryWrapperAsc);
            fundRecordQueryWrapperAsc.clear();
            // endDay Desc
            fundRecordQueryWrapperDesc.select("opendate");
            fundRecordQueryWrapperDesc.orderByDesc("opendate");
            fundRecordQueryWrapperDesc.last("limit 1");
            fundRecordQueryWrapperDesc.eq("code", indexFund.getCode());
            FundRecord endDayOne = fundRecordService.getOne(fundRecordQueryWrapperDesc);
            fundRecordQueryWrapperDesc.clear();
            indexFund.setStartDay(startDayOne.getOpendate());
            indexFund.setEndDay(endDayOne.getOpendate());
            indexFund.setDegree(fundRecordService
                    .calculateDegree(indexFund.getCode(), endDayOne.getOpendate()));
            updateById(indexFund);
        }
        return true;
    }

    @Override
    public List<IndexFund> listByDegreeDesc() {
        QueryWrapper<IndexFund> indexFundQueryWrapper = new QueryWrapper<>();
        indexFundQueryWrapper.orderByDesc("degree");
        return list(indexFundQueryWrapper);
    }

    @Override
    public Boolean updateCount() {
        return baseMapper.updateCount();
    }
}
