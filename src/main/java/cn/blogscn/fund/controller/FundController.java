package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.service.FundService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fund")
public class FundController {
    @Autowired
    private FundService fundService;

    @GetMapping("/list")
    public List<Fund> list(){
        return fundService.list();
    }

    @PostMapping("add")
    public String add(@RequestBody Fund fund){
        return fundService.save(fund)? "success":"false";
    }

}
