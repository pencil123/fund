package cn.blogscn.fund.service.etf.impl;

import cn.blogscn.fund.entity.etf.Indices;
import cn.blogscn.fund.mapper.etf.IndicesMapper;
import cn.blogscn.fund.service.etf.IndicesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IndicesServiceImpl extends ServiceImpl<IndicesMapper, Indices> implements
        IndicesService {

}
