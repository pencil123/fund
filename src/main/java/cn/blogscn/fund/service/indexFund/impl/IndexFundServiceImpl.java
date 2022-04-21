package cn.blogscn.fund.service.indexFund.impl;

import cn.blogscn.fund.entity.indexFund.IndexFund;
import cn.blogscn.fund.entity.indexFund.IndexFundRecord;
import cn.blogscn.fund.mapper.indexFund.IndexFundMapper;
import cn.blogscn.fund.service.indexFund.IndexFundRecordService;
import cn.blogscn.fund.service.indexFund.IndexFundService;
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

@Service
public class IndexFundServiceImpl extends ServiceImpl<IndexFundMapper, IndexFund> implements
        IndexFundService {
    @Autowired
    private IndexFundRecordService indexFundRecordService;

    @Override
    public Boolean updateDegreeAndRate() {
        baseMapper.updateWeekRate();
        baseMapper.updateTwoWeekRate();
        baseMapper.updateMonthRate();
        return baseMapper.updateDegree();
    }

    @Override
    public Boolean updateStartAndEndDay() {
        List<IndexFund> indexFundList = list();
        QueryWrapper<IndexFundRecord> indexFundRecordQueryWrapperAsc = new QueryWrapper<>();
        QueryWrapper<IndexFundRecord> indexFundRecordQueryWrapperDesc = new QueryWrapper<>();
        for (IndexFund indexFund : indexFundList) {
            // startDay Asc
            indexFundRecordQueryWrapperAsc.select("opendate");
            indexFundRecordQueryWrapperAsc.orderByAsc("opendate");
            indexFundRecordQueryWrapperAsc.last("limit 1");
            indexFundRecordQueryWrapperAsc.eq("code", indexFund.getCode());
            IndexFundRecord startDayOne = indexFundRecordService
                    .getOne(indexFundRecordQueryWrapperAsc);
            indexFundRecordQueryWrapperAsc.clear();
            // endDay Desc
            indexFundRecordQueryWrapperDesc.select("opendate");
            indexFundRecordQueryWrapperDesc.orderByDesc("opendate");
            indexFundRecordQueryWrapperDesc.last("limit 1");
            indexFundRecordQueryWrapperDesc.eq("code", indexFund.getCode());
            IndexFundRecord endDayOne = indexFundRecordService
                    .getOne(indexFundRecordQueryWrapperDesc);
            indexFundRecordQueryWrapperDesc.clear();
            if (startDayOne != null) {
                Integer integer = baseMapper.calculateDateDiff(endDayOne.getOpendate());
                if(integer > 3){
                    indexFund.setStopped(true);
                }else{
                    indexFund.setStopped(false);
                }
                indexFund.setStartDay(startDayOne.getOpendate());
                indexFund.setEndDay(endDayOne.getOpendate());
                // update Degree
                updateById(indexFund);
            }
        }
        return true;
    }

    @Override
    public List<IndexFund> listByDegreeDescAndFilter() {
        QueryWrapper<IndexFund> indexFundQueryWrapper = new QueryWrapper<>();
        indexFundQueryWrapper.eq("disabled",false);
        indexFundQueryWrapper.eq("stopped",false);
        indexFundQueryWrapper.orderByDesc("degree");
        return list(indexFundQueryWrapper);
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
        List<IndexFund> list = list();
        for (IndexFund fund : list) {
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

    @Override
    public Boolean disabledAll() {
        return baseMapper.disableAll();
    }


    @Override
    public Boolean batchInsertCodeAndName(List<IndexFund> indexFundList) {
        return baseMapper.batchInsertCodeAndName(indexFundList);
    }
}
