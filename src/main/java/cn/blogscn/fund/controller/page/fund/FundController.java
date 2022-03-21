package cn.blogscn.fund.controller.page.fund;

import cn.blogscn.fund.entity.fund.Fund;
import cn.blogscn.fund.service.fund.FundService;
import cn.blogscn.fund.util.JsonResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fund")
public class FundController {

    @Autowired
    private FundService fundService;

    @GetMapping("/list")
    public JsonResult<List<Fund>> list() {
        return JsonResult.success(fundService.listByDegreeDesc());
    }

    @PostMapping("add")
    public JsonResult<String> add(@RequestBody Fund fund) {
        return fundService.save(fund) ? JsonResult.success() : JsonResult.error();
    }


    @GetMapping("/updateStartAndEndDay")
    public JsonResult<String> updateStartAndEndDay() {
        return fundService.updateStartAndEndDay() ? JsonResult.success("success")
                : JsonResult.error("false");
    }

    @GetMapping("/update-expect")
    public JsonResult<String> updateExpect() {
        fundService.updateExpect();
        return JsonResult.success("Success");
    }

}
