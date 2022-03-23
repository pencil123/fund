package cn.blogscn.fund.service.user;

import cn.blogscn.fund.entity.user.PersistentLogins;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PersistentLoginsService extends IService<PersistentLogins> {

    PersistentLogins selectByUsernameAndSeries(String username, String series);

}
