package cn.blogscn.fund.xxljob.gainian;

import cn.blogscn.fund.model.domain.Gainian;
import cn.blogscn.fund.model.domain.GnRecord;
import cn.blogscn.fund.service.GainianService;
import cn.blogscn.fund.service.GnRecordService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GnRecordUpdateJob {

    private static final Logger logger = LoggerFactory.getLogger(
            GnRecordUpdateJob.class);
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private GainianService gainianService;
    @Autowired
    private GnRecordService gnRecordService;
    private static final String BK_RECORD_URL = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssl_bkzj_zjlrqs";

    @Scheduled(cron = "0 40 23 ? * MON-FRI")
    public void updateGnRecords() throws InterruptedException {
        QueryWrapper<Gainian> gainianQueryWrapper = new QueryWrapper<>();
        gainianQueryWrapper.isNull("start_day");
        List<Gainian> gainainList = gainianService.list(gainianQueryWrapper);
        for (Gainian gainian : gainainList) {
            updateGnRecord(gainian.getCode(), true);
            gainian.setStartDay(getGnRecordStartDay(gainian.getCode()));
            gainian.setEndDay(getGnRecordEndDay(gainian.getCode()));
            gainianService.updateById(gainian);
        }
        updateAvgValueAndDegree();
    }

    private void updateGnRecord(String code, Boolean singlePage) throws InterruptedException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("bankuai", "1/" + code);
        paramMap.put("sort", "opendate");
        paramMap.put("asc", "0");
        paramMap.put("num", "260");
        for (int d = 1; d < 40; d++) {
            paramMap.put("page", d);
            String result = HttpRequest.get(BK_RECORD_URL)
                    .header(Header.REFERER, "http://vip.stock.finance.sina.com.cn/moneyflow/")
                    .form(paramMap)
                    .execute().body();
            JSONArray jsonArray = JSONUtil.parseArray(result);
            if (jsonArray.size() == 0) {
                break;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
                GnRecord gnRecord = parseGnRecordJson(jsonObject, code);
                try {
                    gnRecordService.save(gnRecord);
                } catch (DuplicateKeyException e) {
                    logger.warn("主键冲突数据：{}", gnRecord.toString());
                }
            }
            if (singlePage) {
                break;
            } else {
                Thread.sleep(5000);
            }
        }
    }

    private GnRecord parseGnRecordJson(JSONObject bkObject, String code) {
        GnRecord gnRecord = new GnRecord();
        gnRecord.setCode(code);
        gnRecord.setPrice(BigDecimal.valueOf(bkObject.getDouble("avg_price")));
        gnRecord.setOpendate(LocalDate.parse(bkObject.getStr("opendate"), timeDtf));
        gnRecord.setTurnover(BigDecimal.valueOf(bkObject.getDouble("turnover")));
        gnRecord.setNetamount(BigDecimal.valueOf(bkObject.getDouble("netamount")));
        gnRecord.setR0Net(BigDecimal.valueOf(bkObject.getDouble("r0_net")));
        gnRecord.setR0Ratio(BigDecimal.valueOf(bkObject.getDouble("r0_ratio")));
        gnRecord.setR0xRatio(BigDecimal.valueOf(bkObject.getDouble("r0x_ratio")));
        return gnRecord;
    }

    private LocalDate getGnRecordStartDay(String code) {
        QueryWrapper<GnRecord> gnRecordQueryWrapper = new QueryWrapper<>();
        gnRecordQueryWrapper.eq("code", code);
        gnRecordQueryWrapper.orderByAsc("opendate");
        gnRecordQueryWrapper.last("limit 1");
        GnRecord gnRecord = gnRecordService.getOne(gnRecordQueryWrapper);
        return gnRecord.getOpendate();
    }

    private LocalDate getGnRecordEndDay(String code) {
        QueryWrapper<GnRecord> gnRecordQueryWrapper = new QueryWrapper<>();
        gnRecordQueryWrapper.eq("code", code);
        gnRecordQueryWrapper.orderByDesc("opendate");
        gnRecordQueryWrapper.last("limit 1");
        GnRecord gnRecord = gnRecordService.getOne(gnRecordQueryWrapper);
        return gnRecord.getOpendate();
    }

    public Boolean updateAvgValueAndDegree(){
        gnRecordService.updateAllAvgValue();
        gnRecordService.updateDegree();
        gainianService.updateDegree();
        return true;
    }
}
