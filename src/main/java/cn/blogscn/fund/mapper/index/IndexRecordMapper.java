package cn.blogscn.fund.mapper.index;

import cn.blogscn.fund.entity.index.IndexRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRecordMapper extends BaseMapper<IndexRecord> {

    //select avg(price) from (select price from fund where opendate < ${opendate} and code = ${code} order by opendate desc limit 7) x;
    @Select("select avg(price) from (select price from index_record where opendate <= #{opendate} and code = #{code} order by opendate desc limit 5) x")
    BigDecimal avgWeek(@Param("opendate") String opendate, @Param("code") String fundCode);

    @Select("select avg(price) from (select price from index_record where opendate <= #{opendate} and code = #{code} order by opendate desc limit 20) x")
    BigDecimal avgMonth(@Param("opendate") String opendate, @Param("code") String fundCode);

    @Select("select avg(price) from (select price from index_record where opendate <= #{opendate} and code = #{code} order by opendate desc limit 10) x")
    BigDecimal avgTwoWeek(@Param("opendate") String opendate, @Param("code") String fundCode);

    @Select("select (price-avg_week)*20+(price-avg_two_week)*10+(price-avg_month)*5 from index_record where code = #{code} and opendate = #{opendate}")
    BigDecimal calculateDegree(@Param("opendate") String opendate, @Param("code") String code);

    @Update("update index_record set degree = ((price-avg_week)*20+(price-avg_two_week)*10+(price-avg_month)*5)/price where degree is null")
    Boolean updateDegree();
}
