package cn.blogscn.fund.controller;

import cn.blogscn.fund.xxljob.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.xxljob.fund.SyncData;
import cn.blogscn.fund.xxljob.fund.UpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Autowired
    private SyncData syncData;
    @Autowired
    private UpdateData updateData;
    @Autowired
    private BankuaiUpdateJob bankuaiUpdateJob;

    @GetMapping("/fund/listUpdate")
    public String jobSyncData(){
        syncData.syncFundRecordData();
        return "successs";
    }
    @GetMapping("/fund/dataUpdate")
    public String jobUpdateData(){
        updateData.updateTodayData();
        return "success";
    }
    @GetMapping("/bankuai/listUpdate")
    public String bankuaiListUpdate(){
        bankuaiUpdateJob.updateBankuaiData();
        return "success";
    }
}
