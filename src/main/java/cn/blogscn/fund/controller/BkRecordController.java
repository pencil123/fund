package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.BkRecord;
import cn.blogscn.fund.model.json.JsonResult;
import cn.blogscn.fund.service.BkRecordService;
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
@RequestMapping("/api/bkRecord")
public class BkRecordController {
    @Autowired
    private BkRecordService bkRecordService;

    @GetMapping("/find/list")
    public JsonResult<List<BkRecord>> queryRecordList(@RequestParam("code") String code,
            @RequestParam("startDay") @DateTimeFormat(iso = ISO.DATE) LocalDate startDay, @RequestParam("endDay") @DateTimeFormat(iso = ISO.DATE) LocalDate endDay) {
        List<BkRecord> records = bkRecordService.queryRecordList(code, startDay, endDay);
        return JsonResult.success(records);
    }
}
