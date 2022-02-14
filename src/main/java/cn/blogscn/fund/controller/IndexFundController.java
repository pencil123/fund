package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.IndexFund;
import cn.blogscn.fund.model.json.JsonResult;
import cn.blogscn.fund.service.IndexFundService;
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
    public JsonResult<List<IndexFund>> list(){
        return JsonResult.success(indexFundService.listByDegreeDesc());
    }

    @GetMapping("/updateStartAndEndDay")
    public JsonResult<String> updateStartAndEndDay(){
        return indexFundService.updateStartAndEndDay()? JsonResult.success("success"):JsonResult.error("false");
    }
}
