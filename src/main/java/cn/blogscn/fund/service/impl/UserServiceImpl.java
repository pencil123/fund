package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.UserMapper;
import cn.blogscn.fund.model.domain.User;
import cn.blogscn.fund.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
