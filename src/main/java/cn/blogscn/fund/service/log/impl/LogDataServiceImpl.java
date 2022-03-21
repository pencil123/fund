package cn.blogscn.fund.service.log.impl;

import cn.blogscn.fund.entity.log.LogData;
import cn.blogscn.fund.mapper.log.LogDataMapper;
import cn.blogscn.fund.service.log.LogDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LogDataServiceImpl extends ServiceImpl<LogDataMapper, LogData> implements
        LogDataService {

}
