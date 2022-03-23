package cn.blogscn.fund.service.user.impl;

import cn.blogscn.fund.entity.user.User;
import cn.blogscn.fund.mapper.user.UserMapper;
import cn.blogscn.fund.service.user.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User selectByName(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name", username);
        return getOne(userQueryWrapper, false);
    }
}
