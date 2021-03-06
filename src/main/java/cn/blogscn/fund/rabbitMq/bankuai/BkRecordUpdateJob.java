package cn.blogscn.fund.rabbitMq.bankuai;

import cn.blogscn.fund.entity.bankuai.Bankuai;
import cn.blogscn.fund.entity.bankuai.BkRecord;
import cn.blogscn.fund.entity.log.LogData;
import cn.blogscn.fund.service.bankuai.BankuaiService;
import cn.blogscn.fund.service.bankuai.BkRecordService;
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
public class BkRecordUpdateJob {

    private static final Logger logger = LoggerFactory.getLogger(BkRecordUpdateJob.class);
    private static final String BK_RECORD_URL = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssl_bkzj_zjlrqs";
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private BankuaiService bankuaiService;
    @Autowired
    private BkRecordService bkRecordService;
    @Autowired
    private LogDataService logDataService;

    public Boolean updateBkRecords() {
        logDataService.save(new LogData(this.getClass().getSimpleName(), "板块Record遍历操作:start"));
        List<Bankuai> bankuaiList = bankuaiService.list();
        for (Bankuai bankuai : bankuaiList) {
            if (bankuai.getEndDay() != null && bankuai.getEndDay().equals(LocalDate.now())) {
                logDataService.save(new LogData(this.getClass().getSimpleName(),
                        "板块Record遍历操作:" + bankuai.getName() + ";;skip"));
                return true;
            }
            updateBkRecord(bankuai.getCode());
            bankuai.setStartDay(getBkRecordStartDay(bankuai.getCode()));
            bankuai.setEndDay(getBkRecordEndDay(bankuai.getCode()));
            bankuaiService.updateById(bankuai);
        }
        updateAvgValueAndDegree();
        logDataService.save(new LogData(this.getClass().getSimpleName(), "板块Record遍历操作:end"));
        return true;
    }

    private void updateBkRecord(String code) {
        Boolean pageContinue = true;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("bankuai", "0/" + code);
        paramMap.put("sort", "opendate");
        paramMap.put("asc", "0");
        paramMap.put("num", "10");
        for (int d = 1; d < 40; d++) {
            paramMap.put("page", d);
            String result = HttpRequest.get(BK_RECORD_URL)
                    .header(Header.REFERER, "http://vip.stock.finance.sina.com.cn/moneyflow/")
                    .form(paramMap)
                    .execute().body();
            if (!result.startsWith("[")) {
                logDataService.save(new LogData(this.getClass().getSimpleName(),
                        "Record遍历结果异常,Param:" + paramMap.toString() + "\nResult:" + result));
                continue;
            }
            JSONArray jsonArray = JSONUtil.parseArray(result);
            if (jsonArray.size() == 0) {
                break;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
                BkRecord bkRecord = parseBkRecordJson(jsonObject, code);
                try {
                    bkRecordService.save(bkRecord);
                } catch (DuplicateKeyException e) {
                    pageContinue = false;
                    logger.warn("主键冲突数据：{}", bkRecord.toString());
                }

            }
            if (!pageContinue) {
                break;
            } else {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private BkRecord parseBkRecordJson(JSONObject bkObject, String code) {
        BkRecord bkRecord = new BkRecord();
        bkRecord.setCode(code);
        bkRecord.setPrice(BigDecimal.valueOf(bkObject.getDouble("avg_price")));
        bkRecord.setOpendate(LocalDate.parse(bkObject.getStr("opendate"), timeDtf));
        bkRecord.setTurnover(BigDecimal.valueOf(bkObject.getDouble("turnover")));
        bkRecord.setNetamount(BigDecimal.valueOf(bkObject.getDouble("netamount")));
        bkRecord.setR0Net(BigDecimal.valueOf(bkObject.getDouble("r0_net")));
        bkRecord.setR0Ratio(BigDecimal.valueOf(bkObject.getDouble("r0_ratio")));
        bkRecord.setR0xRatio(BigDecimal.valueOf(bkObject.getDouble("r0x_ratio")));
        return bkRecord;
    }

    private LocalDate getBkRecordStartDay(String code) {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.eq("code", code);
        bkRecordQueryWrapper.orderByAsc("opendate");
        bkRecordQueryWrapper.last("limit 1");
        BkRecord bkRecord = bkRecordService.getOne(bkRecordQueryWrapper);
        return bkRecord == null ? null : bkRecord.getOpendate();
    }

    private LocalDate getBkRecordEndDay(String code) {
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.eq("code", code);
        bkRecordQueryWrapper.orderByDesc("opendate");
        bkRecordQueryWrapper.last("limit 1");
        BkRecord bkRecord = bkRecordService.getOne(bkRecordQueryWrapper);
        return bkRecord == null ? null : bkRecord.getOpendate();
    }


    public Boolean updateAvgValueAndDegree() {
        bkRecordService.updateAllAvgValue();
        bkRecordService.updateDegree();
        bankuaiService.updateDegree();
        return true;
    }
}
