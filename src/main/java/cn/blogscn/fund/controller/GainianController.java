package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.Gainian;
import cn.blogscn.fund.model.json.JsonResult;
import cn.blogscn.fund.service.GainianService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gainian")
public class GainianController {

    @Autowired
    private GainianService gainianService;

    @GetMapping("/list")
    public JsonResult<List<Gainian>> list() {
        return JsonResult.success(gainianService.listByDegreeDesc());
    }

    @GetMapping("/updateStartAndEndDay")
    public JsonResult<String> updateStartAndEndDay() {
        return gainianService.updateStartAndEndDay() ? JsonResult.success("success")
                : JsonResult.error("false");
    }
}
