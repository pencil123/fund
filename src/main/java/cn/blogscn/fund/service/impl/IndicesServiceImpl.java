package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.IndicesMapper;
import cn.blogscn.fund.model.domain.Indices;
import cn.blogscn.fund.service.IndicesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IndicesServiceImpl extends ServiceImpl<IndicesMapper, Indices> implements IndicesService {

}
