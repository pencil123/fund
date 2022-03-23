package cn.blogscn.fund.service.user.impl;

import cn.blogscn.fund.entity.user.PersistentLogins;
import cn.blogscn.fund.entity.user.User;
import cn.blogscn.fund.mapper.user.UserMapper;
import cn.blogscn.fund.service.user.PersistentLoginsService;
import cn.blogscn.fund.service.user.UserService;
import cn.blogscn.fund.util.CookieConstantTable;
import cn.blogscn.fund.util.CookieUtils;
import cn.blogscn.fund.util.EncryptionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PersistentLoginsService persistentLoginsService;

    @Override
    public User selectByName(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name", username);
        return getOne(userQueryWrapper, false);
    }

    @Override
    public User login(User user, HttpServletResponse response) {
        User result = new User();
        // 如果用户名和密码不为空，执行登录
        if (StringUtils.isNotBlank(user.getName()) && StringUtils.isNotBlank(user.getPassword())) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("name",user.getName());
            userQueryWrapper.eq("password",user.getPassword());
            result = getOne(userQueryWrapper, false);
            // 如果rememberme为true，则保存cookie值，下次自动登录
            if (result != null) {
                // 有效期
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime localDateTime = now.plusMonths(1);
                String timeString = localDateTime.format(
                        DateTimeFormatter.ISO_LOCAL_DATE);
                // 为了校验而生成的密文
                String newToken = EncryptionUtil
                        .base64Encode(result.getName() + "_" + result.getPassword() + "_"
                                + timeString + "_" + CookieConstantTable.salt);
                // sha256加密用户信息
                String userInfoBySha256 = EncryptionUtil
                        .base64Encode(result.getName() + "_" + result.getPassword() + "_" + timeString + "_" + CookieConstantTable.salt);
                // UUID值
                String uuidString = UUID.randomUUID().toString();
                // Cookie值
                String cookieValue = EncryptionUtil.base64Encode(result.getName() + ":" + uuidString);

                System.out.println("cookieValue:" + cookieValue);

                // 在数据库中保存自动登录记录（如果已有该用户的记录则更新记录）
                QueryWrapper<PersistentLogins> persistentLoginsQueryWrapper = new QueryWrapper<>();
                persistentLoginsQueryWrapper.eq("username",result.getName());
                PersistentLogins pLogin = persistentLoginsService.getOne(persistentLoginsQueryWrapper,false);
                if (pLogin == null) {
                    pLogin = new PersistentLogins();
                    pLogin.setUsername(result.getName());
                    pLogin.setSeries(uuidString);
                    pLogin.setToken(userInfoBySha256);
                    pLogin.setValidTime(localDateTime);
                    persistentLoginsService.save(pLogin);
                }else{
                    pLogin.setSeries(uuidString);
                    pLogin.setToken(userInfoBySha256);
                    pLogin.setValidTime(localDateTime);
                    persistentLoginsService.updateById(pLogin);
                }
                // 保存cookie
                CookieUtils.addCookie(response, CookieConstantTable.RememberMe, cookieValue, null);
                return result;
            }
            return null;
        }
        return null;
    }
}
