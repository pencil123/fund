package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Fund;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface FundService extends IService<Fund> {
    Boolean  updateDegree();
    Boolean updateStartAndEndDay();
    List<Fund> listByDegreeDesc();
}
