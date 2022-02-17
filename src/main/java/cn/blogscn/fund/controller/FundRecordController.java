package cn.blogscn.fund.controller;

import cn.blogscn.fund.model.domain.FundRecord;
import cn.blogscn.fund.model.json.JsonResult;
import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.util.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
@RequestMapping("/api/fundRecord")
public class FundRecordController {

    @Autowired
    private FundRecordService fundRecordService;

    @GetMapping("/find/page")
    public JsonResult<PageResult<FundRecord>> queryRecordPage(@RequestParam("code") String code,
            @RequestParam(required = false, defaultValue = "1") Long currentPage,
            @RequestParam(required = false, defaultValue = "20") Long pageSize) {

        IPage<FundRecord> recordIPage = fundRecordService
                .queryFundRecordPage(code, currentPage, pageSize);
        PageResult pageResult = new PageResult<>(recordIPage);
        return JsonResult.success(pageResult);
    }

    @GetMapping("/find/list")
    public JsonResult<List<FundRecord>> queryRecordList(@RequestParam("code") String code,
            @RequestParam("startDay") @DateTimeFormat(iso = ISO.DATE) LocalDate startDay,
            @RequestParam("endDay") @DateTimeFormat(iso = ISO.DATE) LocalDate endDay) {
        List<FundRecord> records = fundRecordService.queryFundRecordList(code, startDay, endDay);
        return JsonResult.success(records);
    }

}
