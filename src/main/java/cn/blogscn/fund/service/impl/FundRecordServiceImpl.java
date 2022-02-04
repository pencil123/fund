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
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class FundRecordServiceImpl extends ServiceImpl<FundRecordMapper, FundRecord> implements
        FundRecordService {

    /**
     * 更新每周的均值
     */
    @Override
    public Boolean updateAvgWeek() {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.isNull("avg_week");
        fundRecordQueryWrapper.select("id", "code", "opendate");
        List<FundRecord> list = list(fundRecordQueryWrapper);
        for (FundRecord fundRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgWeek(fundRecord.getOpendate().toString(), fundRecord.getCode());
            fundRecord.setAvgWeek(bigDecimal);
            updateById(fundRecord);
        }
        return true;
    }


    /**
     * 更新每月均值
     */
    @Override
    public Boolean updateAvgMonth() {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.isNull("avg_month");
        fundRecordQueryWrapper.select("id", "code", "opendate");
        List<FundRecord> list = list(fundRecordQueryWrapper);
        for (FundRecord fundRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(fundRecord.getOpendate().toString(), fundRecord.getCode());
            fundRecord.setAvgMonth(bigDecimal);
            updateById(fundRecord);
        }
        return true;
    }

    /**
     * 更新近两周的平均值
     */

    @Override
    public Boolean updateAvgTwoWeek() {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.isNull("avg_two_week");
        fundRecordQueryWrapper.select("id", "code", "opendate");
        List<FundRecord> list = list(fundRecordQueryWrapper);
        for (FundRecord fundRecord : list) {
            BigDecimal bigDecimal = baseMapper.avgTwoWeek(fundRecord.getOpendate().toString(), fundRecord.getCode());
            fundRecord.setAvgTwoWeek(bigDecimal);
            updateById(fundRecord);
        }
        return true;
    }

    @Override
    public IPage<FundRecord> queryFundRecordPage(String code, Long currentPage, Long pageSize) {
        IPage<FundRecord> fundRecordPage = new Page<>(currentPage, pageSize);
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.eq("code",code);
        fundRecordQueryWrapper.orderByDesc("opendate");
        return page(fundRecordPage,fundRecordQueryWrapper);
    }

    @Override
    public List<FundRecord> queryFundRecordList(String code, LocalDate startDay, LocalDate endDay) {
        QueryWrapper<FundRecord> fundRecordQueryWrapper = new QueryWrapper<>();
        fundRecordQueryWrapper.eq("code",code);
        fundRecordQueryWrapper.between("opendate",startDay,endDay);
        fundRecordQueryWrapper.orderByDesc("opendate");
        return list(fundRecordQueryWrapper);
    }

    @Override
    public BigDecimal calculateDegree(String code, LocalDate opendate) {
        return baseMapper.calculateDegree(opendate.toString(), code);
    }
}
