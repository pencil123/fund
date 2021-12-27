package cn.blogscn.fund.service.job;

import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.service.FundService;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UpdateData {
    private static final Logger logger = LoggerFactory.getLogger(UpdateData.class);
    DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private FundService fundService;

    //http://api.fund.eastmoney.com/f10/lsjz?callback=jQuery183019096624312824972_1640614711227&fundCode=519983&pageIndex=1&pageSize=20&startDate=&endDate=&_=1640614711245
    @Scheduled(cron = "0 10 23 ? * MON-FRI")
    public void updateTodayData() {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("callback", "jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode", "519983");
        paramMap.put("pageIndex", "1");
        paramMap.put("pageSize", "10");
        paramMap.put("startDate", "");
        paramMap.put("endDate", "");
        Pattern compile = Pattern.compile("\\((.*)\\)");
        HttpUtil.get("http://api.fund.eastmoney.com/f10/lsjz", paramMap);
        String result12 = HttpRequest.get("http://api.fund.eastmoney.com/f10/lsjz")
                .header(Header.REFERER, "http://fundf10.eastmoney.com/")
                .form(paramMap)
                .execute().body();
        Matcher matcher = compile.matcher(result12);
        matcher.find();
        String group = matcher.group(1);
        JSONObject jsonObject = JSONUtil.parseObj(group);
        JSONObject data = jsonObject.get("Data", JSONObject.class);
        JSONArray lsjzList = data.getJSONArray("LSJZList");
        lsjzList.size();
        for (int i = 0; i < lsjzList.size(); i++) {
            JSONObject fundInfo = lsjzList.get(i, JSONObject.class);
            Fund fund = new Fund();
            System.out.println(fundInfo.toString());
            fund.setFundCode("519983");
            fund.setFsrq(LocalDate.parse(fundInfo.getStr("FSRQ"), timeDtf));
            fund.setDwjz(BigDecimal.valueOf(fundInfo.getDouble("DWJZ")));
            Double jzzzl = fundInfo.getDouble("JZZZL");
            if (jzzzl == null)
                jzzzl = 0.0;
            fund.setJzzzl(BigDecimal.valueOf(jzzzl));
            fund.setLjjz(BigDecimal.valueOf(fundInfo.getDouble("LJJZ")));
            try {
                fundService.save(fund);
            } catch (DuplicateKeyException e) {
                logger.warn("主键冲突数据：{}", fund.toString());
            }
        }
        fundService.updateAvgWeek("519983");
        fundService.updateAvgMonth("519983");
        fundService.updateAvg3month("519983");
    }

}
