package cn.blogscn.fund.mapper.indexFund;

import cn.blogscn.fund.entity.indexFund.IndexFund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexFundMapper extends BaseMapper<IndexFund> {

    @Update("update index_fund g set degree = (select degree from index_fund_record where index_fund_record.opendate = g.end_day and index_fund_record.code = g.code)")
    Boolean updateDegree();

    @Update("update index_fund f\n"
            + "set count = (select count(1) \n"
            + "\tfrom index_fund_record r\n"
            + "\twhere r.code = f.code);")
    Boolean updateCount();

    @Update("update index_fund set disabled = 1")
    Boolean disableAll();

    Boolean batchInsertCodeAndName(@Param("indexFundList") List<IndexFund> indexFundList);

    @Select("select datediff((select opendate from index_record order by opendate desc limit 1),#{endDay})")
    Integer calculateDateDiff(LocalDate endDay);
}
