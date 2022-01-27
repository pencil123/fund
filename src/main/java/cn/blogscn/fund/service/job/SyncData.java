package cn.blogscn.fund.service.job;

import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.model.domain.FundRecord;
import cn.blogscn.fund.service.FundService;
import cn.blogscn.fund.service.FundRecordService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SyncData {
    private static final Logger logger = LoggerFactory.getLogger(SyncData.class);
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private FundRecordService fundRecordService;
    @Autowired
    private FundService fundService;

    public void syncFundRecordData() {
        List<Fund> funds = fundService.list();
        for(Fund fund: funds){
            syncFundRecordData(fund.getFundCode());
        }
    }

    private void syncFundRecordData(String fundCode){
        //http://fundf10.eastmoney.com/jjjz_519983.html
        LocalDate startDate = LocalDate.of(1980, 1, 1);
        LocalDate endDate = startDate.plusDays(20);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("callback", "jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode", fundCode);
        paramMap.put("pageIndex", "1");
        paramMap.put("pageSize", "20");
        paramMap.put("startDate", startDate.toString());
        paramMap.put("endDate", endDate.toString());
        LocalDate localDate = LocalDate.now();
        Pattern compile = Pattern.compile("\\((.*)\\)");
        while (startDate.isBefore(localDate)) {
            startDate = startDate.plusDays(20);
            endDate = endDate.plusDays(20);
            paramMap.put("startDate", startDate.toString());
            paramMap.put("endDate", endDate.toString());
            HttpUtil.get("http://api.fund.eastmoney.com/f10/lsjz", paramMap);
            String result12 = HttpRequest.get("http://api.fund.eastmoney.com/f10/lsjz")
                    .header(Header.REFERER, "http://fundf10.eastmoney.com/")
                    .form(paramMap)
                    .execute().body();
            Matcher matcher = compile.matcher(result12);
            matcher.find();
            String group = matcher.group(1);
            JSONObject jsonObject = JSONUtil.parseObj(group);
            JSONObject data = jsonObject.get("Data", JSONObject.class);
            JSONArray lsjzList = data.getJSONArray("LSJZList");
            lsjzList.size();
            for (int i = 0; i < lsjzList.size(); i++) {
                JSONObject fundRecordInfo = lsjzList.get(i, JSONObject.class);
                FundRecord fundRecord = new FundRecord();
                System.out.println(fundRecordInfo.toString());
                fundRecord.setFundCode(fundCode);
                fundRecord.setFsrq(LocalDate.parse(fundRecordInfo.getStr("FSRQ"), timeDtf));
                fundRecord.setDwjz(BigDecimal.valueOf(fundRecordInfo.getDouble("DWJZ")));
                Double jzzzl = fundRecordInfo.getDouble("JZZZL");
                if (jzzzl == null)
                    jzzzl = 0.0;
                fundRecord.setJzzzl(BigDecimal.valueOf(jzzzl));
                fundRecord.setLjjz(BigDecimal.valueOf(fundRecordInfo.getDouble("LJJZ")));
                try {
                    fundRecordService.save(fundRecord);
                } catch (DuplicateKeyException e) {
                    logger.warn("主键冲突数据：{}", fundRecord.toString());
                }
            }
        }
    }
}
