package cn.blogscn.fund.service.job;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SyncData {

    public void syncFundData(){
        //http://fundf10.eastmoney.com/jjjz_519983.html
        LocalDate startDate = LocalDate.of(1980,1,1);
        LocalDate endDate = startDate.plusDays(20);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("callback","jQuery18305687664236277068_1640352419325");
        paramMap.put("fundCode","519983");
        paramMap.put("pageIndex","1");
        paramMap.put("pageSize","20");
        paramMap.put("startDate",startDate.toString());
        paramMap.put("endDate",endDate.toString());
        LocalDate localDate =  LocalDate.now();
        Pattern compile = Pattern.compile("\\((.*)\\)");
        while(startDate.isBefore(localDate)){
            startDate = startDate.plusDays(20);
            endDate = endDate.plusDays(20);
            paramMap.put("startDate",startDate.toString());
            paramMap.put("endDate",endDate.toString());
            HttpUtil.get("http://api.fund.eastmoney.com/f10/lsjz",paramMap);
            String result12 = HttpRequest.get("http://api.fund.eastmoney.com/f10/lsjz")
                    .header(Header.REFERER,"http://fundf10.eastmoney.com/")
                    .form(paramMap)
                    .execute().body();
            Matcher matcher = compile.matcher(result12);
            matcher.find();
            String group = matcher.group(1);
            JSONObject jsonObject = JSONUtil.parseObj(group);
            JSONObject data = jsonObject.get("Data", JSONObject.class);
            JSONArray lsjzList = data.getJSONArray("LSJZList");
            lsjzList.size();
            for(int i=0;i<lsjzList.size();i++){
                JSONObject jsonObject1 = lsjzList.get(i, JSONObject.class);
                System.out.println(jsonObject1.toString());
            }
        }
    }
}
