package cn.blogscn.fund.xxljob.bankuai;

import cn.blogscn.fund.model.domain.Bankuai;
import cn.blogscn.fund.model.domain.BkRecord;
import cn.blogscn.fund.service.BankuaiService;
import cn.blogscn.fund.service.BkRecordService;
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
import org.springframework.stereotype.Component;

@Component
public class BkRecordUpdateJob {

    private static final Logger logger = LoggerFactory.getLogger(BkRecordUpdateJob.class);
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private BankuaiService bankuaiService;
    @Autowired
    private BkRecordService bkRecordService;
    private static final String BK_RECORD_URL = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssl_bkzj_zjlrqs";

    public void updateBkRecords() throws InterruptedException{
        List<Bankuai> bankuaiList = bankuaiService.list();
        for (Bankuai bankuai : bankuaiList) {
            //updateBkRecord(bankuai.getCode());
            bankuai.setStartDay(getBkRecordStartDay(bankuai.getCode()));
            bankuai.setEndDay(getBkRecordEndDay(bankuai.getCode()));
            bankuaiService.updateById(bankuai);
        }
    }

    public void updateAvgValue(){
        bkRecordService.updateAvgMonth();
        bkRecordService.updateAvgTwoWeek();
        bkRecordService.updateAvgWeek();
    }

    private void updateBkRecord(String code) throws InterruptedException {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("bankuai", "0/" + code);
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
            if(jsonArray.size() ==0) break;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
                BkRecord bkRecord = parseBkRecordJson(jsonObject, code);
                bkRecordService.save(bkRecord);
            }
            logger.info("处理完成：{}",code);
            Thread.sleep(10000);
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

    private LocalDate getBkRecordStartDay(String code){
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.eq("code",code);
        bkRecordQueryWrapper.orderByAsc("opendate");
        bkRecordQueryWrapper.last("limit 1");
        BkRecord bkRecord = bkRecordService.getOne(bkRecordQueryWrapper);
        return bkRecord.getOpendate();
    }

    private LocalDate getBkRecordEndDay(String code){
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        bkRecordQueryWrapper.eq("code",code);
        bkRecordQueryWrapper.orderByDesc("opendate");
        bkRecordQueryWrapper.last("limit 1");
        BkRecord bkRecord = bkRecordService.getOne(bkRecordQueryWrapper);
        return bkRecord.getOpendate();
    }
}
