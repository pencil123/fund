package cn.blogscn.fund.xxljob;

import cn.blogscn.fund.entity.fund.Fund;
import cn.blogscn.fund.entity.indexFund.IndexFund;
import cn.blogscn.fund.service.fund.FundService;
import cn.blogscn.fund.service.indexFund.IndexFundService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckItemStatusJob {

    private static final Logger logger = LoggerFactory.getLogger(CheckItemStatusJob.class);
    @Autowired
    private FundService fundService;
    @Autowired
    private IndexFundService indexFundService;

    @Scheduled(cron = "0 15 9 ? * *")
    public Boolean updateItemStatus() {
        fundService.updateCount();
        indexFundService.updateCount();
        List<Fund> fundList = fundService.list();
        for (Fund fund : fundList) {
            if (fund.getCount().equals(getCount(fund.getCode()))) {
                fund.setStatus(1);
            } else {
                fund.setStatus(0);
            }
            fundService.updateById(fund);
        }

        List<IndexFund> indexFundList = indexFundService.list();
        for (IndexFund indexFund : indexFundList) {
            if (indexFund.getCount().equals(getCount(indexFund.getCode()))) {
                indexFund.setStatus(1);
            } else {
                indexFund.setStatus(0);
            }
            indexFundService.updateById(indexFund);
        }
        return true;
    }

    private Integer getCount(String fundCode) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("callback", "jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode", fundCode);
        paramMap.put("pageIndex", "1");
        paramMap.put("pageSize", "10");
        paramMap.put("startDate", "");
        paramMap.put("endDate", "");
        Pattern compile = Pattern.compile("\\((.*)\\)");
        //HttpUtil.get("http://api.fund.eastmoney.com/f10/lsjz", paramMap);
        String result = HttpRequest.get("http://api.fund.eastmoney.com/f10/lsjz")
                .header(Header.REFERER, "http://fundf10.eastmoney.com/")
                .form(paramMap)
                .execute().body();
        Matcher matcher = compile.matcher(result);
        matcher.find();
        String group = matcher.group(1);
        JSONObject jsonObject = JSONUtil.parseObj(group);
        Integer totalCount = jsonObject.getInt("TotalCount");
        logger.info("Item状态检查（爬取数据）:{}的TotalCount为{}.", fundCode, totalCount);
        return totalCount;
    }
}
