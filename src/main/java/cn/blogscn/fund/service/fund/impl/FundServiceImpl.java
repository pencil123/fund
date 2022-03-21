package cn.blogscn.fund.service.fund.impl;

import cn.blogscn.fund.entity.fund.Fund;
import cn.blogscn.fund.entity.fund.FundRecord;
import cn.blogscn.fund.mapper.fund.FundMapper;
import cn.blogscn.fund.service.fund.FundRecordService;
import cn.blogscn.fund.service.fund.FundService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Override
    public Boolean updateExpect() {
        Pattern compile = Pattern.compile("\\((.*)\\)");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("callback", "jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode", "fundCode");
        paramMap.put("pageSize", "30");
        paramMap.put("pageIndex", "1");
        paramMap.put("startDate", "");
        paramMap.put("endDate", "");
        List<Fund> list = list();
        for (Fund fund : list) {
            paramMap.replace("fundCode", fund.getCode());
            String result = HttpRequest.get("http://api.fund.eastmoney.com/f10/lsjz")
                    .header(Header.REFERER, "http://fundf10.eastmoney.com/")
                    .form(paramMap)
                    .execute().body();
            Matcher matcher = compile.matcher(result);
            matcher.find();
            String group = matcher.group(1);
            JSONObject jsonObject = JSONUtil.parseObj(group);
            Integer totalCount = jsonObject.getInt("TotalCount");
            fund.setExpect(totalCount);
            updateById(fund);
        }
        return true;
    }
}
