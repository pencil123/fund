package cn.blogscn.fund.xxljob.fund;

import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.model.domain.FundRecord;
import cn.blogscn.fund.service.FundService;
import cn.blogscn.fund.service.FundRecordService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class FundRecordDataUpdateJob {
    private static final Logger logger = LoggerFactory.getLogger(FundRecordDataUpdateJob.class);
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private FundRecordService fundRecordService;
    @Autowired
    private FundService fundService;

    public void updateAvgValue(){
        fundRecordService.updateAvgMonth();
        fundRecordService.updateAvgTwoWeek();
        fundRecordService.updateAvgWeek();
    }


    //http://api.fund.eastmoney.com/f10/lsjz?callback=jQuery183019096624312824972_1640614711227&fundCode=519983&pageIndex=1&pageSize=20&startDate=&endDate=&_=1640614711245
    @Scheduled(cron = "0 10 23 ? * MON-FRI")
    public void updateTodayData() {
        List<Fund> funds = fundService.list();
        for(Fund fund: funds){
            String fundCode = fund.getCode();
            updateOne(fundCode);
            fundRecordService.updateAvgWeek();
            fundRecordService.updateAvgMonth();
            fundRecordService.updateAvgTwoWeek();
        }
    }


    private void  updateOne(String fundCode){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("callback", "jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode", fundCode);
        paramMap.put("pageIndex", "1");
        paramMap.put("pageSize", "10");
        paramMap.put("startDate", "");
        paramMap.put("endDate", "");
        Pattern compile = Pattern.compile("\\((.*)\\)");
        //HttpUtil.get("http://api.fund.eastmoney.com/f10/lsjz", paramMap);
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
            fundRecord.setCode(fundCode);
            fundRecord.setOpendate(LocalDate.parse(fundRecordInfo.getStr("FSRQ"), timeDtf));
            fundRecord.setPrice(BigDecimal.valueOf(fundRecordInfo.getDouble("DWJZ")));
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