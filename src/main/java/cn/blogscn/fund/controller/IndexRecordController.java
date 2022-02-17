package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.IndexRecord;
import cn.blogscn.fund.model.json.JsonResult;
import cn.blogscn.fund.service.IndexRecordService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/indexRecord")
public class IndexRecordController {

    @Autowired
    private IndexRecordService indexRecordService;

    @GetMapping("/find/list")
    public JsonResult<List<IndexRecord>> queryRecordList(@RequestParam("code") String code,
            @RequestParam("startDay") @DateTimeFormat(iso = ISO.DATE) LocalDate startDay,
            @RequestParam("endDay") @DateTimeFormat(iso = ISO.DATE) LocalDate endDay) {
        List<IndexRecord> records = indexRecordService.queryRecordList(code, startDay, endDay);
        return JsonResult.success(records);
    }
}
