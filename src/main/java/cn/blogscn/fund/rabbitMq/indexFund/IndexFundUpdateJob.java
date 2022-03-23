package cn.blogscn.fund.rabbitMq.indexFund;

import cn.blogscn.fund.entity.indexFund.IndexFund;
import cn.blogscn.fund.entity.log.LogData;
import cn.blogscn.fund.service.indexFund.IndexFundService;
import cn.blogscn.fund.service.log.LogDataService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexFundUpdateJob {

    private static final Logger logger = LoggerFactory.getLogger(IndexFundUpdateJob.class);
    private static final String BK_URL = "https://xueqiu.com/service/v5/stock/screener/fund/list";
    @Autowired
    private IndexFundService indexFundService;
    @Autowired
    private LogDataService logDataService;

    //?type=17&parent_type=1&order=desc&order_by=percent&page=19&size=30&_=1644654777740

    public Boolean updateIndexFund() {
        LocalDate now = LocalDate.now();
        LocalDateTime startOfTheDay = now.atStartOfDay();
        QueryWrapper<LogData> logDataQueryWrapper = new QueryWrapper<>();
        logDataQueryWrapper.eq("module", this.getClass().getSimpleName());
        logDataQueryWrapper.gt("create_time", startOfTheDay);
        LogData ifExist = logDataService.getOne(logDataQueryWrapper, false);
        if (ifExist != null) {
            logDataService.save(new LogData(this.getClass().getSimpleName(), "指数基金列表更新操作；skip"));
            return true;
        }
        logDataService.save(new LogData(this.getClass().getSimpleName(), "指数基金列表更新操作:start"));
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
        logger.info("获取结果：{}", result);
        JSONObject resultJsonObj = JSONUtil.parseObj(result);
        JSONObject data = resultJsonObj.getJSONObject("data");
        if (data == null) {
            return false;
        }
        JSONArray jsonArray = data.getJSONArray("list");
        indexFundService.disabledAll();
        List<IndexFund> indexFundList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.get(i, JSONObject.class);
            IndexFund indexFund = parseIndexFundJson(jsonObject);
            System.out.println(indexFund.toString());
            indexFundList.add(indexFund);
        }
        indexFundService.batchInsertCodeAndName(indexFundList);
        logDataService.save(new LogData(this.getClass().getSimpleName(), "指数基金列表更新操作:end"));
        return true;
    }

    private IndexFund parseIndexFundJson(JSONObject indexFundObject) {
        IndexFund indexFund = new IndexFund();
        indexFund.setCode(indexFundObject.getStr("symbol").substring(2));
        indexFund.setName(indexFundObject.getStr("name"));
        indexFund.setDisabled(false);
        return indexFund;
    }
}
