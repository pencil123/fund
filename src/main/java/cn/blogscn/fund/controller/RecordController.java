package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.Record;
import cn.blogscn.fund.model.json.JsonResult;
import cn.blogscn.fund.service.RecordService;
import cn.blogscn.fund.util.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/find/page")
    public JsonResult<PageResult<Record>> queryRecordPage(@RequestParam("fundCode") String fundCode,
            @RequestParam(required = false, defaultValue = "1") Long currentPage,
            @RequestParam(required = false, defaultValue = "20") Long pageSize) {

        IPage<Record> recordIPage = recordService.queryRecordPage(fundCode, currentPage, pageSize);
        PageResult pageResult = new PageResult<>(recordIPage);
        return JsonResult.success(pageResult);
    }

    @GetMapping("/find/list")
    public JsonResult<List<Record>> queryRecordList(@RequestParam("fundCode") String fundCode,
            @RequestParam("startDay") LocalDate startDay, @RequestParam("endDay") LocalDate endDay) {

        List<Record> records = recordService.queryRecordList(fundCode, startDay, endDay);
        return JsonResult.success(records);
    }

}
