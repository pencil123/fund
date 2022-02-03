package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.Indices;
import cn.blogscn.fund.model.json.JsonResult;
import cn.blogscn.fund.service.IndicesService;
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
    public JsonResult<List<Indices>> list(){
        return JsonResult.success(indicesService.list());
    }
}
