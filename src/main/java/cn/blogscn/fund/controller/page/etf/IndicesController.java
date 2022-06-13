package cn.blogscn.fund.controller.page.etf;

import cn.blogscn.fund.service.etf.IndicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/indices/")
public class IndicesController {

    @Autowired
    private IndicesService indicesService;

}
