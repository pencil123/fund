package cn.blogscn.fund.interceptors;

import cn.blogscn.fund.entity.user.PersistentLogins;
import cn.blogscn.fund.entity.user.User;
import cn.blogscn.fund.service.user.PersistentLoginsService;
import cn.blogscn.fund.service.user.UserService;
import cn.blogscn.fund.util.CookieConstantTable;
import cn.blogscn.fund.util.CookieUtils;
import cn.blogscn.fund.util.EncryptionUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory
            .getLogger(AuthInterceptor.class);
    @Autowired
    private PersistentLoginsService persistentLoginsService;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return true;
        } else {
            // 从cookie中取值
            Cookie rememberme = CookieUtils.getCookie(request, CookieConstantTable.RememberMe);
            if (rememberme != null) {
                String cookieValue = EncryptionUtil.base64Decode(rememberme.getValue());
                String[] cValues = cookieValue.split(":");
                if (cValues.length == 2) {
                    String usernameByCookie = cValues[0]; // 获取用户名
                    String uuidByCookie = cValues[1]; // 获取UUID值
                    // 到数据库中查询自动登录记录
                    PersistentLogins pLogins = persistentLoginsService.selectByUsernameAndSeries(usernameByCookie,uuidByCookie);
                    if (pLogins != null) {
                        //存在登录记录
                        String savedToken = pLogins.getToken(); // 数据库中保存的密文
                        // 获取有效时间
                        LocalDateTime savedValidtime = pLogins.getValidTime();
                        LocalDateTime now = LocalDateTime.now();
                        // 如果还在cookie有效期之内，继续判断是否可以自动登录
                        if (now.isBefore(savedValidtime)) {
                            //cookie 在有效期内
                            User u = userService.selectByName(usernameByCookie);
                            if (u != null) {
                                // cookie  所属用户存在；自动执行登录过程
                                // 精确到分的时间字符串
                                String timeString = pLogins.getValidTime().format(
                                        DateTimeFormatter.ISO_LOCAL_DATE);
                                // 为了校验而生成的密文
                                String newToken = EncryptionUtil
                                        .base64Encode(u.getName() + "_" + u.getPassword() + "_"
                                                + timeString + "_" + CookieConstantTable.salt);
                                // 校验sha256加密的值，如果不一样则表示用户部分信息已被修改，需要重新登录
                                if (savedToken.equals(newToken)) {
                                    /**
                                     * 为了提高安全性，每次登录之后都更新自动登录的cookie值
                                     */
                                    // 更新cookie值
                                    String uuidNewString = UUID.randomUUID().toString();
                                    String newCookieValue = EncryptionUtil
                                            .base64Encode(u.getName() + ":" + uuidNewString);
                                    CookieUtils.editCookie(request, response,
                                            CookieConstantTable.RememberMe,
                                            newCookieValue, null);

                                    // 更新数据库
                                    pLogins.setSeries(uuidNewString);
                                    persistentLoginsService.updateById(pLogins);
                                    /**
                                     * 将用户加到session中，不退出浏览器时就只需判断session即可
                                     */
                                    session.setAttribute("user", u);
                                    return true;  //校验成功，此次拦截操作完成
                                } else {
                                    // 用户部分信息被修改，删除cookie并清空数据库中的记录
                                    CookieUtils.delCookie(response, rememberme);
                                    persistentLoginsService.removeById(pLogins.getUsername());
                                }
                            }else {
                                //cookie 所属用户不存在
                                CookieUtils.delCookie(response, rememberme);
                                persistentLoginsService.removeById(pLogins.getUsername());
                            }
                        } else {
                            // 超过保存的有效期，删除cookie并清空数据库中的记录
                            CookieUtils.delCookie(response, rememberme);
                            persistentLoginsService.removeById(pLogins.getUsername());
                        }
                    }else{
                        // 不存在登录记录
                        CookieUtils.delCookie(response, rememberme);
                    }
                }
            }
            // 拒绝访问
            response.setStatus(403);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {

    }
}
