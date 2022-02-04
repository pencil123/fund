package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Fund;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FundService extends IService<Fund> {
    Boolean updateStartAndEndDay();
}
