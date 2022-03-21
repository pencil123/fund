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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class IndexFundRecordUpdateFromSohuJob {

    private static final Logger logger = LoggerFactory.getLogger(IndexFundRecordUpdateFromSohuJob.class);
    final private String INDEX_URL = "https://q.stock.sohu.com/hisHq";
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter paramDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter resultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private IndexFundRecordService indexFundRecordService;
    @Autowired
    private IndexFundService indexFundService;
    @Autowired
    private LogDataService logDataService;

    //http://api.fund.eastmoney.com/f10/lsjz?callback=jQuery183019096624312824972_1640614711227&fundCode=519983&pageIndex=1&pageSize=20&startDate=&endDate=&_=1640614711245
    public Boolean updateTodayData() {
        logDataService.save(new LogData(this.getClass().getSimpleName(), "基金Record遍历操作：start"));
        List<IndexFund> indexFunds = indexFundService.list();
        for (IndexFund indexFund : indexFunds) {
            if (indexFund.getEndDay() != null && indexFund.getEndDay().equals(LocalDate.now())) {
                logDataService.save(new LogData(this.getClass().getSimpleName(),
                        "板块Record遍历操作:" + indexFund.getName() + ";;skip"));
                return true;
            }
            fundRecordDataUpdate(indexFund.getCode(), indexFund.getExpect());
        }
        updateAvgValueAndDegree();
        logDataService.save(new LogData(this.getClass().getSimpleName(), "基金Record遍历操作:end"));
        return true;
    }

    private void fundRecordDataUpdate(String code, Integer expect) {
        QueryWrapper<IndexFundRecord> indexFundRecordQueryWrapper = new QueryWrapper<>();
        indexFundRecordQueryWrapper.eq("code", code);
        long count = indexFundRecordService.count(indexFundRecordQueryWrapper);
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", "cn_" + code);
        paramMap.put("0.7805789106973908", "");
        Boolean firstRequest = true;
        int tryTime = 0;
        do {
            paramMap.put("start", firstDayOfMonth.format(paramDateFormat));
            if (firstRequest) {
                paramMap.put("end", today.format(paramDateFormat));
                firstRequest = false;
            } else {
                paramMap.put("end", lastDayOfMonth.format(paramDateFormat));
            }
            String result = HttpRequest.get(INDEX_URL)
                    .header(Header.REFERER, "https://q.stock.sohu.com")
                    .form(paramMap)
                    .execute().body();
            if (!result.startsWith("[")) {
                firstDayOfMonth = firstDayOfMonth.minusMonths(1);
                lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
                logger.info("解析存在问题,result:{},param:{}", result,
                        code + "--" + firstDayOfMonth.toString() + "---" + lastDayOfMonth
                                .toString());
                tryTime++;
                if (tryTime == 10) {
                    break;
                }
                continue;
            }
            try {
                JSONArray jsonArray = JSONUtil.parseArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (!jsonObject.getStr("status").equals("0")) {
                    logger.info("请求响应信息：{}", result);
                    break;
                }
                JSONArray hq = jsonObject.getJSONArray("hq");
                parseJsonArray(hq, code);// 调用对象解析和数据存储；
                firstDayOfMonth = firstDayOfMonth.minusMonths(1);
                lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
            } catch (Exception e) {
                logger.error(e.toString());
                logger.info("请求响应信息：{}", result);
            }
            count = indexFundRecordService.count(indexFundRecordQueryWrapper);
        } while (count < expect);
    }

    private Boolean parseJsonArray(JSONArray indexRecords, String code) {
        for (int i = 0; i < indexRecords.size(); i++) {
            IndexFundRecord indexFundRecord = new IndexFundRecord();
            indexFundRecord.setCode(code);
            JSONArray indexInfo = indexRecords.getJSONArray(i);
            indexFundRecord.setOpendate(LocalDate.parse(indexInfo.getStr(0), resultDateFormat));
            indexFundRecord.setPrice(BigDecimal.valueOf(indexInfo.getDouble(2)));
            indexFundRecord.setVolume(BigDecimal.valueOf(indexInfo.getInt(7)));
            indexFundRecord.setTurnover(BigDecimal.valueOf(indexInfo.getDouble(8)));
            try {
                indexFundRecordService.save(indexFundRecord);
            } catch (DuplicateKeyException e) {
                //logger.warn("主键冲突数据：{}", indexFundRecord.toString());
            }
        }
        return true;
    }

    public Boolean updateAvgValueAndDegree() {
        indexFundRecordService.updateAllAvgValue();
        indexFundRecordService.updateDegree();
        indexFundService.updateDegree();
        indexFundService.updateStartAndEndDay();
        return true;
    }
}
