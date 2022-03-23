package cn.blogscn.fund.service.user;

import cn.blogscn.fund.entity.user.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    User selectByName(String username);
}
