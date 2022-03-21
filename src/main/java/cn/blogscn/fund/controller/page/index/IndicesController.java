package cn.blogscn.fund.controller.page.index;

import cn.blogscn.fund.entity.index.Indices;
import cn.blogscn.fund.service.index.IndicesService;
import cn.blogscn.fund.util.JsonResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/indices")
public class IndicesController {

    @Autowired
    private IndicesService indicesService;

    @GetMapping("/list")
    public JsonResult<List<Indices>> list() {
        return JsonResult.success(indicesService.listByDegreeDesc());
    }

    @GetMapping("/updateStartAndEndDay")
    public JsonResult<String> updateStartAndEndDay() {
        return indicesService.updateStartAndEndDay() ? JsonResult.success("success")
                : JsonResult.error("false");
    }
}
