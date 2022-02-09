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
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BankuaiUpdateJob {
    private static final Logger logger = LoggerFactory.getLogger(BankuaiUpdateJob.class);
    @Autowired
    private BankuaiService bankuaiService;
    @Autowired
    private LogDataService logDataService;
    private static final String BK_URL = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssl_bkzj_bk";

    public Boolean updateBankuaiData(){
        logger.info("定时任务：遍历板块列表Start");
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
        JSONArray jsonArray = JSONUtil.parseArray(result);
        for(int i=0;i< jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
            Bankuai bankuai = parseBankuaiJson(jsonObject);
            try {
                bankuaiService.save(bankuai);
            } catch (DuplicateKeyException e) {
                logger.warn("主键冲突数据：{}", bankuai.toString());
            }
        }
        logger.info("定时任务：遍历板块列表End");
        logDataService.save(new LogData(this.getClass().getSimpleName(),"板块遍历完成(获取80个)；板块数为：" + jsonArray.size()));
        return true;
    }

    private Bankuai parseBankuaiJson(JSONObject bankuaiObject){
        Bankuai bankuai = new Bankuai();
        bankuai.setCode(bankuaiObject.getStr("category"));
        bankuai.setName(bankuaiObject.getStr("name"));
        return bankuai;
    }

}
