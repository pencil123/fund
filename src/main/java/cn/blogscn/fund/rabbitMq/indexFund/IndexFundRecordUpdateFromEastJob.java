package cn.blogscn.fund.rabbitMq.indexFund;

import cn.blogscn.fund.entity.indexFund.IndexFund;
import cn.blogscn.fund.entity.indexFund.IndexFundRecord;
import cn.blogscn.fund.entity.log.LogData;
import cn.blogscn.fund.service.indexFund.IndexFundRecordService;
import cn.blogscn.fund.service.indexFund.IndexFundService;
import cn.blogscn.fund.service.log.LogDataService;
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
public class IndexFundRecordUpdateFromEastJob {

    private static final Logger logger = LoggerFactory
            .getLogger(IndexFundRecordUpdateFromEastJob.class);
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private IndexFundService indexFundService;
    @Autowired
    private IndexFundRecordService indexFundRecordService;
    @Autowired
    private LogDataService logDataService;

    //http://api.fund.eastmoney.com/f10/lsjz?callback=jQuery183019096624312824972_1640614711227&fundCode=519983&pageIndex=1&pageSize=20&startDate=&endDate=&_=1640614711245
    public Boolean updateTodayData() {
        logDataService
                .save(new LogData(this.getClass().getSimpleName(), "基金IndexRecord遍历操作：start"));
        List<IndexFund> indexFunds = indexFundService.list();
        for (IndexFund indexFund : indexFunds) {
            if (indexFund.getEndDay() != null && indexFund.getEndDay().equals(LocalDate.now())) {
                logDataService.save(new LogData(this.getClass().getSimpleName(),
                        "板块IndexRecord遍历操作:" + indexFund.getName() + ";;skip"));
                continue;
            }
            updateOne(indexFund.getCode(), indexFund.getBackward());
        }
        updateAvgValueAndDegree();
        logDataService.save(new LogData(this.getClass().getSimpleName(), "基金IndexRecord遍历操作:end"));
        return true;
    }

    private void updateOne(String indexFundCode, Boolean status) {
        HashMap<String, Object> paramMap = new HashMap<>();
        Integer pageIndex = 1;
        Pattern compile = Pattern.compile("\\((.*)\\)");
        paramMap.put("callback", "jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode", indexFundCode);
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
            status = lsjzList.size() == 0;
            parseItems(lsjzList, indexFundCode);
            pageIndex++;
        } while (!status);
    }

    private Boolean parseItems(JSONArray lsjzList, String fundCode) {
        ArrayList<IndexFundRecord> indexFundRecords = new ArrayList<>();
        for (int i = 0; i < lsjzList.size(); i++) {
            JSONObject fundRecordInfo = lsjzList.get(i, JSONObject.class);
            IndexFundRecord indexFundRecord = new IndexFundRecord();
            try {
                indexFundRecord.setCode(fundCode);
                indexFundRecord
                        .setOpendate(LocalDate.parse(fundRecordInfo.getStr("FSRQ"), timeDtf));
                indexFundRecord.setPrice(BigDecimal.valueOf(fundRecordInfo.getDouble("DWJZ")));
                Double jzzzl = fundRecordInfo.getDouble("JZZZL");
                if (jzzzl == null) {
                    jzzzl = 0.0;
                }
                indexFundRecord.setJzzzl(BigDecimal.valueOf(jzzzl));
                Double ljjz = fundRecordInfo.getDouble("LJJZ");
                if (ljjz == null) {
                    ljjz = 0.0;
                }
                indexFundRecord.setLjjz(BigDecimal.valueOf(ljjz));
                indexFundRecords.add(indexFundRecord);
            } catch (Exception e) {
                logger.error("fundRecordInfo:{}", fundRecordInfo.toString());
                logger.error(e.toString());
            }
        }
        if (indexFundRecords.size() == 0) {
            return false;
        }
        return indexFundRecordService.batchInsertOrUpdateJz(indexFundRecords);
    }


    public Boolean updateAvgValueAndDegree() {
        indexFundRecordService.updateAllAvgValue();
        indexFundRecordService.updateDegree();
        indexFundService.updateDegreeAndRate();
        indexFundService.updateStartAndEndDay();
        return true;
    }
}
