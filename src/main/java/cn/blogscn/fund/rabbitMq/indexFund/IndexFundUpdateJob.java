package cn.blogscn.fund.rabbitMq.indexFund;

import cn.blogscn.fund.model.domain.IndexFund;
import cn.blogscn.fund.model.domain.LogData;
import cn.blogscn.fund.service.IndexFundService;
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
import org.springframework.stereotype.Component;

@Component
public class IndexFundUpdateJob {
    private static final Logger logger = LoggerFactory.getLogger(IndexFundUpdateJob.class);
    @Autowired
    private IndexFundService indexFundService;
    @Autowired
    private LogDataService logDataService;
    private static final String BK_URL = "https://xueqiu.com/service/v5/stock/screener/fund/list";

    //?type=17&parent_type=1&order=desc&order_by=percent&page=19&size=30&_=1644654777740

    public Boolean updateIndexFund(){
        logger.info("定时任务：遍历板块列表Start");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", "17");
        paramMap.put("parent_type", "1");
        paramMap.put("order", "desc");
        paramMap.put("order_by", "percent");
        paramMap.put("size", "900");
        paramMap.put("page", "1");
        String result = HttpRequest.get(BK_URL)
                .header(Header.REFERER, "https://xueqiu.com/hq")
                .form(paramMap)
                .execute().body();
        logger.info("获取结果：{}",result);
        JSONObject resultJsonObj = JSONUtil.parseObj(result);
        JSONObject data = resultJsonObj.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("list");
        for(int i=0;i< jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
            IndexFund indexFund = parseIndexFundJson(jsonObject);
            System.out.println(indexFund.toString());
            try {
                indexFundService.save(indexFund);
            } catch (DuplicateKeyException e) {
                logger.warn("主键冲突数据：{}", indexFund.toString());
            }
        }
        logger.info("定时任务：遍历板块列表End");
        //logDataService.save(new LogData(this.getClass().getSimpleName(),"板块遍历完成(获取80个)；板块数为：" + jsonArray.size()));
        return true;
    }

    private IndexFund parseIndexFundJson(JSONObject indexFundObject){
        IndexFund indexFund = new IndexFund();
        indexFund.setCode(indexFundObject.getStr("symbol").substring(2));
        indexFund.setName(indexFundObject.getStr("name"));
        return indexFund;
    }
}
