package cn.blogscn.fund.xxljob;

import cn.blogscn.fund.dto.ThirdApi.EtfResultDto;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class EtfRecordJob {

    private static final Logger logger = LoggerFactory.getLogger(EtfRecordJob.class);
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public boolean syncEtfRecord() {
        ArrayList<EtfResultDto> etfResultDtos = new ArrayList<>();
        JSONObject paramJson = new JSONObject();
        paramJson.set("___jsl", "LST___t=1654750925782");
        paramJson.set("rp", "25");
        paramJson.set("page", "1");
        String response = HttpUtil
                .post("https://www.jisilu.cn/data/etf/etf_list/", paramJson.toString());
        JSONObject responseJson = JSONUtil.parseObj(response);
        JSONArray jsonArray = responseJson.getJSONArray("rows");
        for (Object obj : jsonArray) {
            JSONObject jsonObject = JSONUtil.parseObj(obj);
            JSONObject cell = jsonObject.getJSONObject("cell");
            etfResultDtos.add(parseObject(cell));
        }
        for(EtfResultDto etfResultDto:etfResultDtos){
            logger.info(etfResultDto.toString());
        }
        return true;
    }
    private EtfResultDto parseObject(JSONObject jsonObject){
        EtfResultDto etfResultDto = new EtfResultDto();
        etfResultDto.setCode(jsonObject.getInt("fund_id"));
        etfResultDto.setName(jsonObject.getStr("fund_nm"));
        etfResultDto.setIssuer(jsonObject.getStr("issuer_nm"));
        etfResultDto.setAmount(jsonObject.getInt("amount"));
        etfResultDto.setUnitTotal(jsonObject.getBigDecimal("unit_total"));
        etfResultDto.setUnitIncr(jsonObject.getBigDecimal("unit_incr"));
        etfResultDto.setPrice(jsonObject.getBigDecimal("price"));
        etfResultDto.setVolume(jsonObject.getBigDecimal("volume"));
        etfResultDto.setDate(LocalDate.parse(jsonObject.getStr("last_dt"),fmt));
        etfResultDto.setIncreaseRt(jsonObject.getBigDecimal("increase_rt"));
        etfResultDto.setEstimateValue(jsonObject.getBigDecimal("estimate_value"));
        etfResultDto.setDiscountRt(jsonObject.getBigDecimal("discount_rt"));
        etfResultDto.setFundNav(jsonObject.getBigDecimal("fund_nav"));
        etfResultDto.setNavDate(LocalDate.parse(jsonObject.getStr("nav_dt"),fmt));
        etfResultDto.setIndexName(jsonObject.getStr("index_nm"));
        etfResultDto.setIndexIcreaseRt(jsonObject.getBigDecimal("index_increase_rt"));
        return etfResultDto;
    }
}
