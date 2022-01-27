package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.FundRecordMapper;
import cn.blogscn.fund.model.domain.FundRecord;
import cn.blogscn.fund.service.FundRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class FundRecordServiceImpl extends ServiceImpl<FundRecordMapper, FundRecord> implements
        FundRecordService {

    /**
     * 更新每周的均值
     */
    @Override
    public Boolean updateAvgWeek(String fundCode) {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.isNull("avg_week");
        fundRecordQueryWrapper.select("id", "fund_code", "fsrq");
        List<FundRecord> list = list(fundRecordQueryWrapper);
        for (FundRecord fundRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgWeek(fundRecord.getFsrq().toString(), fundRecord.getFundCode());
            fundRecord.setAvgWeek(bigDecimal);
            updateById(fundRecord);
        }
        return null;
    }


    /**
     * 更新每月均值
     */
    @Override
    public Boolean updateAvgMonth(String fundCode) {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.isNull("avg_month");
        fundRecordQueryWrapper.select("id", "fund_code", "fsrq");
        List<FundRecord> list = list(fundRecordQueryWrapper);
        for (FundRecord fundRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(fundRecord.getFsrq().toString(), fundRecord.getFundCode());
            fundRecord.setAvgMonth(bigDecimal);
            updateById(fundRecord);
        }
        return null;
    }

    /**
     * 更新三个月均值
     */

    @Override
    public Boolean updateAvg3month(String fundCode) {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.isNull("avg3month");
        fundRecordQueryWrapper.select("id", "fund_code", "fsrq");
        List<FundRecord> list = list(fundRecordQueryWrapper);
        for (FundRecord fundRecord : list) {
            BigDecimal bigDecimal = baseMapper.avg3month(fundRecord.getFsrq().toString(), fundRecord.getFundCode());
            fundRecord.setAvg3month(bigDecimal);
            updateById(fundRecord);
        }
        return null;
    }

    @Override
    public IPage<FundRecord> queryFundRecordPage(String fundCode, Long currentPage, Long pageSize) {
        IPage<FundRecord> fundRecordPage = new Page<>(currentPage, pageSize);
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.eq("fund_code",fundCode);
        fundRecordQueryWrapper.orderByDesc("fsrq");
        return page(fundRecordPage,fundRecordQueryWrapper);
    }

    @Override
    public List<FundRecord> queryFundRecordList(String fundCode, LocalDate startDay, LocalDate endDay) {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.eq("fund_code",fundCode);
        fundRecordQueryWrapper.between("fsrq",startDay,endDay);
        fundRecordQueryWrapper.orderByDesc("fsrq");
        return list(fundRecordQueryWrapper);
    }
}
