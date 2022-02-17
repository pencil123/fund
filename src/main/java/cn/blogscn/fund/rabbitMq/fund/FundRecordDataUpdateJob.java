package cn.blogscn.fund.rabbitMq.fund;

import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.model.domain.FundRecord;
import cn.blogscn.fund.model.domain.IndexFund;
import cn.blogscn.fund.model.domain.LogData;
import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.service.FundService;
import cn.blogscn.fund.service.IndexFundService;
import cn.blogscn.fund.service.LogDataService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FundRecordDataUpdateJob {

    private static final Logger logger = LoggerFactory.getLogger(FundRecordDataUpdateJob.class);
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private FundRecordService fundRecordService;
    @Autowired
    private FundService fundService;
    @Autowired
    private IndexFundService indexFundService;
    @Autowired
    private LogDataService logDataService;

    //http://api.fund.eastmoney.com/f10/lsjz?callback=jQuery183019096624312824972_1640614711227&fundCode=519983&pageIndex=1&pageSize=20&startDate=&endDate=&_=1640614711245
    public Boolean updateTodayData() {
        logDataService.save(new LogData(this.getClass().getSimpleName(), "基金Record遍历操作：start"));
        List<Fund> funds = fundService.list();
        List<IndexFund> indexFunds = indexFundService.list();
        for (Fund fund : funds) {
            updateOne(fund.getCode(), fund.getStatus());
        }
        for (IndexFund indexFund : indexFunds) {
            updateOne(indexFund.getCode(), indexFund.getStatus());
        }
        updateAvgValueAndDegree();
        logDataService.save(new LogData(this.getClass().getSimpleName(), "基金Record遍历操作:end"));
        return true;
    }

    private void updateOne(String fundCode, Integer status) {
        HashMap<String, Object> paramMap = new HashMap<>();
        Integer pageIndex = 1;
        Pattern compile = Pattern.compile("\\((.*)\\)");
        paramMap.put("callback", "jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode", fundCode);
        paramMap.put("pageSize", "30");
        paramMap.put("startDate", "");
        paramMap.put("endDate", "");
        do {
            paramMap.put("pageIndex", pageIndex);
            //HttpUtil.get("http://api.fund.eastmoney.com/f10/lsjz", paramMap);
            String result = HttpRequest.get("http://api.fund.eastmoney.com/f10/lsjz")
                    .header(Header.REFERER, "http://fundf10.eastmoney.com/")
                    .form(paramMap)
                    .execute().body();
            Matcher matcher = compile.matcher(result);
            matcher.find();
            String group = matcher.group(1);
            JSONObject jsonObject = JSONUtil.parseObj(group);
            JSONObject data = jsonObject.get("Data", JSONObject.class);
            JSONArray lsjzList = data.getJSONArray("LSJZList");
            status = lsjzList.size() == 0 ? 1 : 0;
            parseItems(lsjzList, fundCode);
            pageIndex++;
        } while (status == 0);
    }

    private Boolean parseItems(JSONArray lsjzList, String fundCode) {
        ArrayList<FundRecord> fundRecords = new ArrayList<>();
        for (int i = 0; i < lsjzList.size(); i++) {
            JSONObject fundRecordInfo = lsjzList.get(i, JSONObject.class);
            FundRecord fundRecord = new FundRecord();
            try {
                fundRecord.setCode(fundCode);
                fundRecord.setOpendate(LocalDate.parse(fundRecordInfo.getStr("FSRQ"), timeDtf));
                fundRecord.setPrice(BigDecimal.valueOf(fundRecordInfo.getDouble("DWJZ")));
                Double jzzzl = fundRecordInfo.getDouble("JZZZL");
                if (jzzzl == null) {
                    jzzzl = 0.0;
                }
                fundRecord.setJzzzl(BigDecimal.valueOf(jzzzl));
                Double ljjz = fundRecordInfo.getDouble("LJJZ");
                if (ljjz == null) {
                    ljjz = 0.0;
                }
                fundRecord.setLjjz(BigDecimal.valueOf(ljjz));
                fundRecords.add(fundRecord);
            } catch (Exception e) {
                logger.error("fundRecordInfo:{}", fundRecordInfo.toString());
                logger.error(e.toString());
            }
        }
        if (fundRecords.size() == 0) {
            return false;
        }
        return fundRecordService.batchInsert(fundRecords);
    }


    public Boolean updateAvgValueAndDegree() {
        fundRecordService.updateAllAvgValue();
        fundRecordService.updateDegree();
        fundService.updateDegree();
        fundService.updateStartAndEndDay();
        indexFundService.updateDegree();
        indexFundService.updateStartAndEndDay();
        return true;
    }
}
