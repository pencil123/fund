package cn.blogscn.fund.rabbitMq.bankuai;

import cn.blogscn.fund.model.domain.Bankuai;
import cn.blogscn.fund.model.domain.LogData;
import cn.blogscn.fund.service.BankuaiService;
import cn.blogscn.fund.service.LogDataService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BankuaiUpdateJob {

    private static final Logger logger = LoggerFactory.getLogger(BankuaiUpdateJob.class);
    private static final String BK_URL = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssl_bkzj_bk";
    @Autowired
    private BankuaiService bankuaiService;
    @Autowired
    private LogDataService logDataService;

    public Boolean updateBankuaiData() {
        LocalDate now = LocalDate.now();
        LocalDateTime startOfTheDay = now.atStartOfDay();
        QueryWrapper<LogData> logDataQueryWrapper = new QueryWrapper<>();
        logDataQueryWrapper.eq("module",this.getClass().getSimpleName());
        logDataQueryWrapper.gt("create_time",startOfTheDay);
        LogData ifExist = logDataService.getOne(logDataQueryWrapper);
        if(ifExist != null){
            logDataService.save(new LogData(this.getClass().getSimpleName(), "板块遍历完成(获取80个)；skip"));
            return true;
        }
        logDataService.save(new LogData(this.getClass().getSimpleName(), "板块遍历完成(获取80个)；start"));
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("sort", "netamount");
        paramMap.put("asc", "0");
        paramMap.put("fenlei", "0");
        paramMap.put("num", "80");
        paramMap.put("page", "1");
        String result = HttpRequest.get(BK_URL)
                .header(Header.REFERER, "http://vip.stock.finance.sina.com.cn/moneyflow/")
                .form(paramMap)
                .execute().body();
        //logger.info("获取结果：{}",result);
        if (!result.startsWith("[")) {
            logger.info("解析存在问题,result:{}", result);
            return false;
        }
        JSONArray jsonArray = JSONUtil.parseArray(result);
        ArrayList<Bankuai> bankuais = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
            Bankuai bankuai = parseBankuaiJson(jsonObject);
            bankuais.add(bankuai);
        }
        bankuaiService.batchInsert(bankuais);
        logDataService.save(new LogData(this.getClass().getSimpleName(),
                "板块遍历完成(获取80个)；板块数为：" + jsonArray.size()));
        return true;
    }

    private Bankuai parseBankuaiJson(JSONObject bankuaiObject) {
        Bankuai bankuai = new Bankuai();
        bankuai.setCode(bankuaiObject.getStr("category"));
        bankuai.setName(bankuaiObject.getStr("name"));
        return bankuai;
    }

}
