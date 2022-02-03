package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.BkRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface BkRecordMapper  extends BaseMapper<BkRecord> {
    //select avg(price) from (select price from fund where opendate < ${opendate} and code = ${code} order by opendate desc limit 7) x;
    @Select("select avg(price) from (select price from bk_record where opendate < #{opendate} and code = #{code} order by opendate desc limit 5) x")
    BigDecimal avgWeek(@Param("opendate")String opendate,@Param("code") String fundCode);

    @Select("select avg(price) from (select price from bk_record where opendate < #{opendate} and code = #{code} order by opendate desc limit 20) x")
    BigDecimal avgMonth(@Param("opendate")String opendate,@Param("code") String fundCode);

    @Select("select avg(price) from (select price from bk_record where opendate < #{opendate} and code = #{code} order by opendate desc limit 10) x")
    BigDecimal avgTwoWeek(@Param("opendate")String opendate,@Param("code") String fundCode);
}