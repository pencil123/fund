package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.FundMapper;
import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.service.FundService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;


@Component
public class FundServiceImpl extends ServiceImpl<FundMapper, Fund> implements FundService {
    /**
     * 更新每周的均值
     */
    @Override
    public Boolean updateAvgWeek(String fundCode) {
        QueryWrapper<Fund> fundQueryWrapper = new QueryWrapper<>();
        fundQueryWrapper.isNull("avg_week");
        fundQueryWrapper.select("id", "fund_code", "fsrq");
        List<Fund> list = list(fundQueryWrapper);
        for (Fund fund : list) {
            BigDecimal bigDecimal = baseMapper.avgWeek(fund.getFsrq().toString(), fund.getFundCode());
            fund.setAvgWeek(bigDecimal);
            updateById(fund);
        }
        return null;
    }


    /**
     * 更新每月均值
     */
    @Override
    public Boolean updateAvgMonth(String fundCode) {
        QueryWrapper<Fund> fundQueryWrapper = new QueryWrapper<>();
        fundQueryWrapper.isNull("avg_month");
        fundQueryWrapper.select("id", "fund_code", "fsrq");
        List<Fund> list = list(fundQueryWrapper);
        for (Fund fund : list) {
            BigDecimal bigDecimal = baseMapper.avgMonth(fund.getFsrq().toString(), fund.getFundCode());
            fund.setAvgMonth(bigDecimal);
            updateById(fund);
        }
        return null;
    }

    /**
     * 更新三个月均值
     */

    @Override
    public Boolean updateAvg3month(String fundCode) {
        QueryWrapper<Fund> fundQueryWrapper = new QueryWrapper<>();
        fundQueryWrapper.isNull("avg3month");
        fundQueryWrapper.select("id", "fund_code", "fsrq");
        List<Fund> list = list(fundQueryWrapper);
        for (Fund fund : list) {
            BigDecimal bigDecimal = baseMapper.avg3month(fund.getFsrq().toString(), fund.getFundCode());
            fund.setAvg3month(bigDecimal);
            updateById(fund);
        }
        return null;
    }
}
