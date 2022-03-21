package cn.blogscn.fund.service.fund;

import cn.blogscn.fund.entity.fund.Fund;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface FundService extends IService<Fund> {

    Boolean updateDegree();

    Boolean updateStartAndEndDay();

    List<Fund> listByDegreeDesc();

    Boolean updateCount();

    Boolean updateExpect();
}
