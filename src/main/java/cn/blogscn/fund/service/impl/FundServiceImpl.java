package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.FundMapper;
import cn.blogscn.fund.model.domain.Fund;
import cn.blogscn.fund.service.FundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class FundServiceImpl extends ServiceImpl<FundMapper, Fund> implements FundService {

}
