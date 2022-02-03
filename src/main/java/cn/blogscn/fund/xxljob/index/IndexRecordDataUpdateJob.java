package cn.blogscn.fund.xxljob.index;

import cn.blogscn.fund.model.domain.IndexRecord;
import cn.blogscn.fund.model.domain.Indices;
import cn.blogscn.fund.service.IndexRecordService;
import cn.blogscn.fund.service.IndicesService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
public class IndexRecordDataUpdateJob {

    @Autowired
    private IndicesService indicesService;
    @Autowired
    private IndexRecordService indexRecordService;
    DateTimeFormatter paramDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter resultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final LocalDate startDay = LocalDate.of(1991, 1, 1);
    final private String INDEX_URL = "https://q.stock.sohu.com/hisHq";
    private static final Logger logger = LoggerFactory.getLogger(IndexRecordDataUpdateJob.class);

    public void indexRecordDataUpdateMain() {
        List<Indices> list = indicesService.list();
        for (Indices indices : list) {
            indexRecordDataUpdate(indices.getCode());
        }
    }

    private void indexRecordDataUpdate(String code) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", "zs_" + code);
        Boolean firstRequest = true;
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
            if(!result.startsWith("[")){
                firstDayOfMonth = firstDayOfMonth.minusMonths(1);
                lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
                logger.info("解析存在问题,result:{}", result);
                continue;
            }
            try {
                JSONArray jsonArray = JSONUtil.parseArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONArray hq = jsonObject.getJSONArray("hq");
                parseJsonArray(hq,code);// 调用对象解析和数据存储；
                firstDayOfMonth = firstDayOfMonth.minusMonths(1);
                lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
                logger.info("将要爬取的数据：firstDay:{},endDay:{}", firstDayOfMonth, lastDayOfMonth);
            } catch (Exception e) {
                logger.error(e.toString());
                logger.info("请求响应信息：{}",result);
            }
        } while (firstDayOfMonth.isAfter(startDay));
    }

    private Boolean parseJsonArray(JSONArray indexRecords,String code){
        IndexRecord indexRecord = new IndexRecord();
        indexRecord.setCode(code);
        for (int i = 0; i < indexRecords.size(); i++) {
            JSONArray indexInfo = indexRecords.getJSONArray(i);
            indexRecord.setOpendate(LocalDate.parse(indexInfo.getStr(0),resultDateFormat));
            indexRecord.setPrice(BigDecimal.valueOf(indexInfo.getDouble(2)));
            indexRecord.setVolume(Long.valueOf(indexInfo.getInt(7)));
            indexRecord.setTurnover(BigDecimal.valueOf(indexInfo.getDouble(8)));
            try {
                indexRecordService.save(indexRecord);
            }catch (DuplicateKeyException e) {
                logger.warn("主键冲突数据：{}", indexRecord.toString());
            }
        }
        return true;
    }



    public void updateAvgValue(){
        indexRecordService.updateAvgMonth();
        indexRecordService.updateAvgTwoWeek();
        indexRecordService.updateAvgWeek();
    }
}
