package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.Fund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface FundMapper extends BaseMapper<Fund> {
    @Update("update fund g set degree = (select degree from fund_record where fund_record.opendate = g.end_day and fund_record.code = g.code)")
    Boolean updateDegree();
}
