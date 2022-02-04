package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Indices;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IndicesService extends IService<Indices> {
    Boolean updateStartAndEndDay();

}
