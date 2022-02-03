package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.BankuaiMapper;
import cn.blogscn.fund.model.domain.Bankuai;
import cn.blogscn.fund.service.BankuaiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankuaiServiceImpl extends ServiceImpl<BankuaiMapper, Bankuai> implements BankuaiService {

}