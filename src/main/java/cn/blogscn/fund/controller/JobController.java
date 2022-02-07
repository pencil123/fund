package cn.blogscn.fund.controller;

import cn.blogscn.fund.service.BkRecordService;
import cn.blogscn.fund.service.FundRecordService;
import cn.blogscn.fund.service.GnRecordService;
import cn.blogscn.fund.service.IndexRecordService;
import cn.blogscn.fund.xxljob.SendMailJob;
import cn.blogscn.fund.xxljob.bankuai.BankuaiUpdateJob;
import cn.blogscn.fund.xxljob.bankuai.BkRecordUpdateJob;
import cn.blogscn.fund.xxljob.fund.FundDataInitJob;
import cn.blogscn.fund.xxljob.fund.FundRecordDataUpdateJob;
import cn.blogscn.fund.xxljob.gainian.GainianUpdateJob;
import cn.blogscn.fund.xxljob.gainian.GnRecordUpdateJob;
import cn.blogscn.fund.xxljob.index.IndexRecordDataUpdateJob;
import javax.mail.MessagingException;
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
    private GainianUpdateJob gainianUpdateJob;
    @Autowired
    private GnRecordUpdateJob gnRecordUpdateJob;
    @Autowired
    private BkRecordUpdateJob bkRecordUpdateJob;
    @Autowired
    IndexRecordDataUpdateJob indexRecordDataUpdateJob;
    @Autowired
    private SendMailJob sendMailJob;

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
    @GetMapping("/gainian/listUpdate")
    public String gainianListUpdate(){
        gainianUpdateJob.updateGainianData();
        return "success";
    }

    @GetMapping("/gainain/RecordUpdate")
    public String gainianRecordUpdate() throws InterruptedException {
        gnRecordUpdateJob.updateGnRecords();
        return "success";
    }

    @GetMapping("/indices/dataUpdate")
    public String indicesDataUpdate(){
        indexRecordDataUpdateJob.indexRecordDataUpdateMain();
        return "success";
    }

    @GetMapping("/updateAvgValue")
    public String updateAvgValue(){
        bkRecordUpdateJob.updateAvgValueAndDegree();
        fundRecordDataUpdateJob.updateAvgValueAndDegree();
        gnRecordUpdateJob.updateAvgValueAndDegree();
        indexRecordDataUpdateJob.updateAvgValueAndDegree();
        return "success";
    }

    @GetMapping("/dataMailSend")
    public String dataMailSend() throws MessagingException {
        sendMailJob.sendMail();
        return "success";
    }
}
