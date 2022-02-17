package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.LogDataMapper;
import cn.blogscn.fund.model.domain.LogData;
import cn.blogscn.fund.service.LogDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LogDataServiceImpl extends ServiceImpl<LogDataMapper, LogData> implements
        LogDataService {

}
