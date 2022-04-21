package cn.blogscn.fund.controller.page.indexFund;

import cn.blogscn.fund.dto.IndexWithRateDto;
import cn.blogscn.fund.entity.indexFund.IndexFund;
import cn.blogscn.fund.service.indexFund.IndexFundService;
import cn.blogscn.fund.util.JsonResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/indexFund")
public class IndexFundController {

    @Autowired
    private IndexFundService indexFundService;

    @GetMapping("/list")
    public JsonResult<List<IndexFund>> list(
            QueryWrapper<IndexFund> indexFundQueryWrapper) {
        return JsonResult.success(indexFundService.listByDegreeDescAndFilter());
    }

    @GetMapping("/updateStartAndEndDay")
    public JsonResult<String> updateStartAndEndDay() {
        return indexFundService.updateStartAndEndDay() ? JsonResult.success("success")
                : JsonResult.error("false");
    }

    @GetMapping("/update-expect")
    public JsonResult<String> updateExpect() {
        indexFundService.updateExpect();
        return JsonResult.success("Success");
    }

    @GetMapping("/listWithRate")
    public JsonResult<List<IndexWithRateDto>> listWithRate(){
        QueryWrapper<IndexFund> indexFundQueryWrapper = new QueryWrapper<>();
        indexFundQueryWrapper.eq("disabled",false);
        indexFundQueryWrapper.eq("stopped",false);
        indexFundQueryWrapper.orderByDesc("recent_week_rate");
        List<IndexFund> recentWeekRateList = indexFundService.list(indexFundQueryWrapper);
        indexFundQueryWrapper.clear();
        indexFundQueryWrapper.eq("disabled",false);
        indexFundQueryWrapper.eq("stopped",false);
        indexFundQueryWrapper.orderByDesc("recent_two_week_rate");
        List<IndexFund> recentTwoWeekRateList = indexFundService.list(indexFundQueryWrapper);
        indexFundQueryWrapper.clear();
        indexFundQueryWrapper.eq("disabled",false);
        indexFundQueryWrapper.eq("stopped",false);
        indexFundQueryWrapper.orderByDesc("recent_month_rate");
        List<IndexFund> recentMonthRateList = indexFundService.list(indexFundQueryWrapper);
        ArrayList<IndexWithRateDto> indexWithRateDtos = new ArrayList<IndexWithRateDto>();
        for(int i =0;i < recentMonthRateList.size();i++){
            IndexWithRateDto indexWithRateDto = new IndexWithRateDto();
            indexWithRateDto.setWeekCode(recentWeekRateList.get(i).getCode());
            indexWithRateDto.setWeekName(recentWeekRateList.get(i).getName());
            indexWithRateDto.setWeekRate(recentWeekRateList.get(i).getRecentWeekRate());
            indexWithRateDto.setTwoWeekCode(recentTwoWeekRateList.get(i).getCode());
            indexWithRateDto.setTwoWeekName(recentTwoWeekRateList.get(i).getName());
            indexWithRateDto.setTwoWeekRate(recentTwoWeekRateList.get(i).getRecentTwoWeekRate());
            indexWithRateDto.setMonthCode(recentMonthRateList.get(i).getCode());
            indexWithRateDto.setMonthName(recentMonthRateList.get(i).getName());
            indexWithRateDto.setMonthRate(recentMonthRateList.get(i).getRecentMonthRate());
            indexWithRateDtos.add(indexWithRateDto);
        }
        return JsonResult.success(indexWithRateDtos);
    }
}
