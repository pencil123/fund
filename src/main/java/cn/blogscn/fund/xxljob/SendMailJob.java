package cn.blogscn.fund.xxljob;
import cn.blogscn.fund.model.domain.Bankuai;
import cn.blogscn.fund.model.domain.BkRecord;
import cn.blogscn.fund.model.domain.Gainian;
import cn.blogscn.fund.model.domain.GnRecord;
import cn.blogscn.fund.model.domain.IndexRecord;
import cn.blogscn.fund.model.domain.Indices;
import cn.blogscn.fund.model.domain.User;
import cn.blogscn.fund.model.dts.BkRecordDto;
import cn.blogscn.fund.model.dts.EmailRecordDto;
import cn.blogscn.fund.model.dts.IndexRecordDto;
import cn.blogscn.fund.service.BankuaiService;
import cn.blogscn.fund.service.BkRecordService;
import cn.blogscn.fund.service.GainianService;
import cn.blogscn.fund.service.GnRecordService;
import cn.blogscn.fund.service.IndexRecordService;
import cn.blogscn.fund.service.IndicesService;
import cn.blogscn.fund.service.UserService;
import cn.blogscn.fund.util.BeanConvertUtils;
import cn.blogscn.fund.util.MailSend;
import cn.blogscn.fund.xxljob.index.IndexRecordDataUpdateJob;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class SendMailJob {
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
    private MailSend mailSend;
    private static final Logger logger = LoggerFactory.getLogger(SendMailJob.class);


    @Scheduled(cron = "0 10 4 ? * MON-FRI")
    public void sendMail() throws MessagingException {
        List<EmailRecordDto> indexRecordDtos = new ArrayList<>();
        List<Indices> indicesList = indicesService.listByDegreeDesc();
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        for(Indices indices:indicesList){
            indexRecordQueryWrapper.eq("code",indices.getCode());
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
        for(Gainian gainian:gainians){
            gnRecordQueryWrapper.eq("code",gainian.getCode());
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
        for(Bankuai bankuai:bankuais){
            bkRecordQueryWrapper.eq("code",bankuai.getCode());
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
        // 设置模板中的变量
        // 第一个参数为模板的名称
        context.setVariable("bkRecordDtos",bkRecordDtos);
        context.setVariable("indexRecordDtos", indexRecordDtos);
        context.setVariable("gnRecordDtos",gnRecordDtos);
        List<User> userList = userService.list();
        for(User user:userList){
            mailSend.sendThymeleafMail("明天会议安排",context,user.getEmail());
        }
    }
}
