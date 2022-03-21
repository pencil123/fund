package cn.blogscn.fund.mapper.fund;

import cn.blogscn.fund.entity.fund.Fund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface FundMapper extends BaseMapper<Fund> {

    @Update("update fund g set degree = (select degree from fund_record where fund_record.opendate = g.end_day and fund_record.code = g.code)")
    Boolean updateDegree();

    @Update("update fund f\n"
            + "set count = (select count(1) \n"
            + "\tfrom fund_record r\n"
            + "\twhere r.code = f.code);")
    Boolean updateCount();
}
