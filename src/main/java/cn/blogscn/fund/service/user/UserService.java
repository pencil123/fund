package cn.blogscn.fund.service.user;

import cn.blogscn.fund.entity.user.User;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {

    User selectByName(String username);


    User login(User user, HttpServletResponse response);
}
