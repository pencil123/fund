package cn.blogscn.fund.service.indexFund;

import cn.blogscn.fund.entity.indexFund.IndexFund;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IndexFundService extends IService<IndexFund> {

    Boolean updateDegreeAndRate();

    Boolean updateStartAndEndDay();

    List<IndexFund> listByDegreeDescAndFilter();

    Boolean updateCount();

    Boolean updateExpect();

    Boolean disabledAll();


    Boolean batchInsertCodeAndName(List<IndexFund> indexFundList);
}
