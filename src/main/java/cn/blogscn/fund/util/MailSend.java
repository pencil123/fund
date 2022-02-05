package cn.blogscn.fund.util;

import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MailSend {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendThymeleafMail(String title,Context context,String mailWillTo) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(title);
        helper.setFrom(mailFrom);
        helper.setTo(mailWillTo);
        helper.setSentDate(new Date());
        String process = templateEngine.process("emailTemplate.html", context);
        // 第二个参数true表示这是一个html文本
        helper.setText(process,true);
        javaMailSender.send(mimeMessage);
    }
    public void sendSimpleMail() {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject("这是一封测试邮件");
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom("tiger@5teams.cn");
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        message.setTo("10*****16@qq.com","12****32*qq.com");
        // 设置邮件抄送人，可以有多个抄送人
        //message.setCc("12****32*qq.com");
        // 设置隐秘抄送人，可以有多个
        // message.setBcc("7******9@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText("这是测试邮件的正文");
        // 发送邮件
        javaMailSender.send(message);
    }

}
