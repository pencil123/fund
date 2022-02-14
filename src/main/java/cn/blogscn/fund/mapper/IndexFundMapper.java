package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.IndexFund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexFundMapper extends BaseMapper<IndexFund> {
    @Update("update index_fund g set degree = (select degree from fund_record where fund_record.opendate = g.end_day and fund_record.code = g.code)")
    Boolean updateDegree();

    @Update("update index_fund f\n"
            + "set count = (select count(1) \n"
            + "\tfrom fund_record r\n"
            + "\twhere r.code = f.code);")
    Boolean updateCount();
}
