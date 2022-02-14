package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.IndexFund;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IndexFundService extends IService<IndexFund> {
    Boolean  updateDegree();
    Boolean updateStartAndEndDay();
    List<IndexFund> listByDegreeDesc();
    Boolean updateCount();
}
