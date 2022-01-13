package cn.blogscn.fund.controller;

import cn.blogscn.fund.service.job.SyncData;
import cn.blogscn.fund.service.job.UpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private SyncData syncData;
    @Autowired
    private UpdateData updateData;

    @GetMapping("/job/sync")
    public String jobSyncData(){
        syncData.syncRecordData();
        return "successs";
    }
    @GetMapping("/job/update")
    public String jobUpdateData(){
        updateData.updateTodayData();
        return "success";
    }
}
