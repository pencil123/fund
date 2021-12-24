package cn.blogscn.fund.service.job;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SyncData {

    public void syncFundData(){
        //http://fundf10.eastmoney.com/jjjz_519983.html
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("callback","jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode","519983");
        paramMap.put("pageIndex","1");
        paramMap.put("pageSize","20");
        paramMap.put("startDate","2021-12-01");
        paramMap.put("endDate","2021-12-14");

        HttpUtil.get("http://api.fund.eastmoney.com/f10/lsjz",paramMap);

        String result12 = HttpRequest.get("http://api.fund.eastmoney.com/f10/lsjz")
                .header(Header.REFERER,"http://fundf10.eastmoney.com/")
                .form(paramMap)
                .execute().body();

        System.out.println(result12);
    }
}
