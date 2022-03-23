package cn.blogscn.fund.service.user.impl;

import cn.blogscn.fund.entity.user.PersistentLogins;
import cn.blogscn.fund.mapper.user.PersistentLoginsMapper;
import cn.blogscn.fund.service.user.PersistentLoginsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PersistentLoginsServiceImpl extends
        ServiceImpl<PersistentLoginsMapper, PersistentLogins> implements PersistentLoginsService {

    @Override
    public PersistentLogins selectByUsernameAndSeries(String username, String series) {
        QueryWrapper<PersistentLogins> persistentLoginsQueryWrapper = new QueryWrapper<>();
        persistentLoginsQueryWrapper.eq("username", username);
        persistentLoginsQueryWrapper.eq("series", series);
        return getOne(persistentLoginsQueryWrapper, false);
    }


}
