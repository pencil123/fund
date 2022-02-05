package cn.blogscn.fund.xxljob;
import cn.blogscn.fund.model.domain.Bankuai;
import cn.blogscn.fund.model.domain.BkRecord;
import cn.blogscn.fund.model.domain.IndexRecord;
import cn.blogscn.fund.model.domain.Indices;
import cn.blogscn.fund.model.domain.User;
import cn.blogscn.fund.model.dts.BkRecordDto;
import cn.blogscn.fund.model.dts.IndexRecordDto;
import cn.blogscn.fund.service.BankuaiService;
import cn.blogscn.fund.service.BkRecordService;
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
    private MailSend mailSend;
    private static final Logger logger = LoggerFactory.getLogger(SendMailJob.class);


    @Scheduled(cron = "0 10 23 ? * MON-FRI")
    public void sendMail() throws MessagingException {
        List<IndexRecordDto> indexRecordDtos = new ArrayList<>();
        List<Indices> indicesList = indicesService.listByDegreeDesc();
        QueryWrapper<IndexRecord> indexRecordQueryWrapper = new QueryWrapper<>();
        for(Indices indices:indicesList){
            indexRecordQueryWrapper.eq("code",indices.getCode());
            indexRecordQueryWrapper.orderByDesc("opendate");
            indexRecordQueryWrapper.last("limit 1");
            IndexRecord indexRecord = indexRecordService.getOne(indexRecordQueryWrapper);
            indexRecordQueryWrapper.clear();
            IndexRecordDto indexRecordDto = BeanConvertUtils
                    .copyProperties(indexRecord, IndexRecordDto.class);
            indexRecordDto.setName(indices.getName());
            indexRecordDto.setDegree(indices.getDegree());
            indexRecordDtos.add(indexRecordDto);
        }

        List<BkRecordDto> bkRecordDtos = new ArrayList<>();
        List<Bankuai> bankuais = bankuaiService.listByDegreeDesc();
        QueryWrapper<BkRecord> bkRecordQueryWrapper = new QueryWrapper<>();
        for(Bankuai bankuai:bankuais){
            bkRecordQueryWrapper.eq("code",bankuai.getCode());
            bkRecordQueryWrapper.orderByDesc("opendate");
            bkRecordQueryWrapper.last("limit 1");
            BkRecord bkRecord = bkRecordService.getOne(bkRecordQueryWrapper);
            bkRecordQueryWrapper.clear();
            BkRecordDto bkRecordDto = BeanConvertUtils.copyProperties(bkRecord, BkRecordDto.class);
            bkRecordDto.setName(bankuai.getName());
            bkRecordDto.setDegree(bankuai.getDegree());
            bkRecordDtos.add(bkRecordDto);
        }

        Context context = new Context();
        // 设置模板中的变量
        // 第一个参数为模板的名称
        context.setVariable("bkRecordDtos",bkRecordDtos);
        context.setVariable("indexRecordDtos", indexRecordDtos);
        List<User> userList = userService.list();
        for(User user:userList){
            mailSend.sendThymeleafMail("明天会议安排",context,user.getEmail());
        }
    }
}
