package cn.blogscn.fund.xxljob;
import cn.blogscn.fund.model.domain.User;
import cn.blogscn.fund.service.UserService;
import cn.blogscn.fund.util.MailSend;
import java.util.List;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class SendMailJob {
    @Autowired
    private UserService userService;
    @Autowired
    private MailSend mailSend;
    public void sendMail() throws MessagingException {
        List<User> userList = userService.list();
        for(User user:userList){
            Context context = new Context();
            // 设置模板中的变量
            // 第一个参数为模板的名称
            context.setVariable("username", "javaboy");
            context.setVariable("num","000001");
            context.setVariable("salary", "99999");
            mailSend.sendThymeleafMail("明天会议安排",context,user.getEmail());
        }
    }
}
