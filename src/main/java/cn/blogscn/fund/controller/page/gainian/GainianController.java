package cn.blogscn.fund.controller.page.gainian;

import cn.blogscn.fund.entity.gainian.Gainian;
import cn.blogscn.fund.service.gainian.GainianService;
import cn.blogscn.fund.util.JsonResult;
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
