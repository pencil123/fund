package cn.blogscn.fund.controller;

import cn.blogscn.fund.xxljob.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.xxljob.bankuai.BkRecordUpdateJob;
import cn.blogscn.fund.xxljob.fund.FundDataInitJob;
import cn.blogscn.fund.xxljob.fund.FundRecordDataUpdateJob;
import cn.blogscn.fund.xxljob.index.IndexRecordDataUpdateJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Autowired
    private FundDataInitJob fundDataInitJob;
    @Autowired
    private FundRecordDataUpdateJob fundRecordDataUpdateJob;
    @Autowired
    private BankuaiUpdateJob bankuaiUpdateJob;
    @Autowired
    private BkRecordUpdateJob bkRecordUpdateJob;
    @Autowired
    IndexRecordDataUpdateJob indexRecordDataUpdateJob;

    @GetMapping("/fund/listUpdate")
    public String jobSyncData(){
        fundDataInitJob.syncFundRecordData();
        return "successs";
    }
    @GetMapping("/fund/dataUpdate")
    public String jobUpdateData(){
        fundRecordDataUpdateJob.updateTodayData();
        return "success";
    }
    @GetMapping("/bankuai/listUpdate")
    public String bankuaiListUpdate(){
        bankuaiUpdateJob.updateBankuaiData();
        return "success";
    }

    @GetMapping("/indices/dataUpdate")
    public String indicesDataUpdate(){
        indexRecordDataUpdateJob.indexRecordDataUpdateMain();
        return "success";
    }
}
