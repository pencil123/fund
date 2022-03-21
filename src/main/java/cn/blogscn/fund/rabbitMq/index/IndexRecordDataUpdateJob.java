package cn.blogscn.fund.rabbitMq.index;

import cn.blogscn.fund.entity.index.IndexRecord;
import cn.blogscn.fund.entity.index.Indices;
import cn.blogscn.fund.entity.log.LogData;
import cn.blogscn.fund.service.index.IndexRecordService;
import cn.blogscn.fund.service.index.IndicesService;
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
public class IndexRecordDataUpdateJob {

    private static final Logger logger = LoggerFactory.getLogger(IndexRecordDataUpdateJob.class);
    final LocalDate startDay = LocalDate.of(1991, 1, 1);
    final private String INDEX_URL = "https://q.stock.sohu.com/hisHq";
    DateTimeFormatter paramDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter resultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private IndicesService indicesService;
    @Autowired
    private IndexRecordService indexRecordService;
    @Autowired
    private LogDataService logDataService;

    public Boolean indexRecordDataUpdateMain() {
        logDataService.save(new LogData(this.getClass().getSimpleName(), "指标Record遍历操作:start"));
        List<Indices> list = indicesService.list();
        for (Indices indices : list) {
            if (indices.getEndDay() != null && indices.getEndDay().equals(LocalDate.now())) {
                logDataService.save(new LogData(this.getClass().getSimpleName(),
                        "指标Record遍历操作:" + indices.getName() + ";;skip"));
                return true;
            }
            indexRecordDataUpdate(indices.getCode());
            indices.setEndDay(getEndDay(indices.getCode()));
            indices.setStartDay(getStartDay(indices.getCode()));
            indicesService.updateById(indices);
        }
        updateAvgValueAndDegree();
        logDataService.save(new LogData(this.getClass().getSimpleName(), "指标Record遍历操作:end"));
        return true;
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
            if (!result.startsWith("[")) {
                firstDayOfMonth = firstDayOfMonth.minusMonths(1);
                lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
                logger.info("解析存在问题,result:{}", result);
                continue;
            }
            try {
                JSONArray jsonArray = JSONUtil.parseArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONArray hq = jsonObject.getJSONArray("hq");
                parseJsonArray(hq, code);// 调用对象解析和数据存储；
                firstDayOfMonth = firstDayOfMonth.minusMonths(1);
                lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
                logger.info("将要爬取的数据：firstDay:{},endDay:{}", firstDayOfMonth, lastDayOfMonth);
            } catch (Exception e) {
                logger.error(e.toString());
                logger.info("请求响应信息：{}", result);
            }
        } while (firstDayOfMonth.isAfter(startDay));
    }

    private Boolean parseJsonArray(JSONArray indexRecords, String code) {
        IndexRecord indexRecord = new IndexRecord();
        indexRecord.setCode(code);
        for (int i = 0; i < indexRecords.size(); i++) {
            JSONArray indexInfo = indexRecords.getJSONArray(i);
            indexRecord.setOpendate(LocalDate.parse(indexInfo.getStr(0), resultDateFormat));
            indexRecord.setPrice(BigDecimal.valueOf(indexInfo.getDouble(2)));
            indexRecord.setVolume(Long.valueOf(indexInfo.getInt(7)));
            indexRecord.setTurnover(BigDecimal.valueOf(indexInfo.getDouble(8)));
            try {
                indexRecordService.save(indexRecord);
            } catch (DuplicateKeyException e) {
                logger.warn("主键冲突数据：{}", indexRecord.toString());
            }
        }
        return true;
    }

    private LocalDate getStartDay(String code) {
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        indexRecordQueryWrapper.eq("code", code);
        indexRecordQueryWrapper.orderByAsc("opendate");
        indexRecordQueryWrapper.last("limit 1");
        IndexRecord indexRecord = indexRecordService.getOne(indexRecordQueryWrapper);
        return indexRecord.getOpendate();
    }

    private LocalDate getEndDay(String code) {
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        indexRecordQueryWrapper.eq("code", code);
        indexRecordQueryWrapper.orderByDesc("opendate");
        indexRecordQueryWrapper.last("limit 1");
        IndexRecord indexRecord = indexRecordService.getOne(indexRecordQueryWrapper);
        return indexRecord.getOpendate();
    }


    public Boolean updateAvgValueAndDegree() {
        indexRecordService.updateAllAvgValue();
        indexRecordService.updateDegree();
        indicesService.updateDegree();
        return true;
    }
}
