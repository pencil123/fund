package cn.blogscn.fund.controller.page.indexFund;

import cn.blogscn.fund.entity.indexFund.IndexFundRecord;
import cn.blogscn.fund.service.indexFund.IndexFundRecordService;
import cn.blogscn.fund.util.JsonResult;
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
@RequestMapping("/api/index-fund-record")
public class IndexFundRecordController {
    @Autowired
    private IndexFundRecordService indexFundRecordService;

    @GetMapping("/find/list")
    public JsonResult<List<IndexFundRecord>> queryRecordList(@RequestParam("code") String code,
            @RequestParam("startDay") @DateTimeFormat(iso = ISO.DATE) LocalDate startDay,
            @RequestParam("endDay") @DateTimeFormat(iso = ISO.DATE) LocalDate endDay) {
        List<IndexFundRecord> records = indexFundRecordService.queryFundRecordList(code, startDay, endDay);
        return JsonResult.success(records);
    }
}
