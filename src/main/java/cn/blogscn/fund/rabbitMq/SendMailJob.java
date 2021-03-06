package cn.blogscn.fund.rabbitMq;

import cn.blogscn.fund.dto.EmailRecordDto;
import cn.blogscn.fund.entity.bankuai.Bankuai;
import cn.blogscn.fund.entity.bankuai.BkRecord;
import cn.blogscn.fund.entity.gainian.Gainian;
import cn.blogscn.fund.entity.gainian.GnRecord;
import cn.blogscn.fund.entity.index.IndexRecord;
import cn.blogscn.fund.entity.index.Indices;
import cn.blogscn.fund.entity.log.LogData;
import cn.blogscn.fund.entity.user.User;
import cn.blogscn.fund.service.bankuai.BankuaiService;
import cn.blogscn.fund.service.bankuai.BkRecordService;
import cn.blogscn.fund.service.gainian.GainianService;
import cn.blogscn.fund.service.gainian.GnRecordService;
import cn.blogscn.fund.service.index.IndexRecordService;
import cn.blogscn.fund.service.index.IndicesService;
import cn.blogscn.fund.service.log.LogDataService;
import cn.blogscn.fund.service.user.UserService;
import cn.blogscn.fund.util.BeanConvertUtils;
import cn.blogscn.fund.util.MailSend;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class SendMailJob {

    private static final Logger logger = LoggerFactory.getLogger(SendMailJob.class);
    @Autowired
    private UserService userService;
    @Autowired
    private IndicesService indicesService;
    @Autowired
    private IndexRecordService indexRecordService;
    @Autowired
    private BankuaiService bankuaiService;
    @Autowired
    private BkRecordService bkRecordService;
    @Autowired
    private GainianService gainianService;
    @Autowired
    private GnRecordService gnRecordService;
    @Autowired
    private LogDataService logDataService;
    @Autowired
    private MailSend mailSend;

    public Boolean sendMail() {
        logDataService.save(new LogData(this.getClass().getSimpleName(), "??????????????????:start"));
        List<EmailRecordDto> indexRecordDtos = new ArrayList<>();
        List<Indices> indicesList = indicesService.listByDegreeDesc();
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        for (Indices indices : indicesList) {
            indexRecordQueryWrapper.eq("code", indices.getCode());
            indexRecordQueryWrapper.orderByDesc("opendate");
            indexRecordQueryWrapper.last("limit 1");
            IndexRecord indexRecord = indexRecordService.getOne(indexRecordQueryWrapper);
            indexRecordQueryWrapper.clear();
            EmailRecordDto emailRecordDto = BeanConvertUtils
                    .copyProperties(indexRecord, EmailRecordDto.class);
            emailRecordDto.setName(indices.getName());
            emailRecordDto.setDegree(indices.getDegree());
            indexRecordDtos.add(emailRecordDto);
        }

        List<EmailRecordDto> gnRecordDtos = new ArrayList<>();
        List<Gainian> gainians = gainianService.listByDegreeDesc();
        QueryWrapper<GnRecord> gnRecordQueryWrapper = new QueryWrapper<>();
        for (Gainian gainian : gainians) {
            gnRecordQueryWrapper.eq("code", gainian.getCode());
            gnRecordQueryWrapper.orderByDesc("opendate");
            gnRecordQueryWrapper.last("limit 1");
            GnRecord gnRecord = gnRecordService.getOne(gnRecordQueryWrapper);
            gnRecordQueryWrapper.clear();
            EmailRecordDto emailRecordDto = BeanConvertUtils
                    .copyProperties(gnRecord, EmailRecordDto.class);
            emailRecordDto.setName(gainian.getName());
            emailRecordDto.setDegree(gainian.getDegree());
            gnRecordDtos.add(emailRecordDto);
        }

        List<EmailRecordDto> bkRecordDtos = new ArrayList<>();
        List<Bankuai> bankuais = bankuaiService.listByDegreeDesc();
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        for (Bankuai bankuai : bankuais) {
            bkRecordQueryWrapper.eq("code", bankuai.getCode());
            bkRecordQueryWrapper.orderByDesc("opendate");
            bkRecordQueryWrapper.last("limit 1");
            BkRecord bkRecord = bkRecordService.getOne(bkRecordQueryWrapper);
            bkRecordQueryWrapper.clear();
            EmailRecordDto emailRecordDto = BeanConvertUtils
                    .copyProperties(bkRecord, EmailRecordDto.class);
            emailRecordDto.setName(bankuai.getName());
            emailRecordDto.setDegree(bankuai.getDegree());
            bkRecordDtos.add(emailRecordDto);
        }

        Context context = new Context();
        // ????????????????????????
        // ?????????????????????????????????
        context.setVariable("bkRecordDtos", bkRecordDtos);
        context.setVariable("indexRecordDtos", indexRecordDtos);
        context.setVariable("gnRecordDtos", gnRecordDtos);
        List<User> userList = userService.list();
        for (User user : userList) {
            try {
                mailSend.sendThymeleafMail("??????????????????", context, user.getEmail());
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
        }
        logDataService.save(new LogData(this.getClass().getSimpleName(), "??????????????????:end"));
        return true;
    }
}
