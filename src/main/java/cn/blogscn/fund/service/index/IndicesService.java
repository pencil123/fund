package cn.blogscn.fund.service.index;

import cn.blogscn.fund.entity.index.Indices;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IndicesService extends IService<Indices> {

    Boolean updateDegree();

    Boolean updateStartAndEndDay();

    List<Indices> listByDegreeDesc();
}
