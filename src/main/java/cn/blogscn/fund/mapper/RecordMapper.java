package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface RecordMapper extends BaseMapper<Record> {
    //select avg(dwjz) from (select dwjz from fund where fsrq < ${fsrq} and fund_code = ${fund_code} order by fsrq desc limit 7) x;
    @Select("select avg(dwjz) from (select dwjz from record where fsrq < #{fsrq} and fund_code = #{fund_code} order by fsrq desc limit 5) x")
    BigDecimal avgWeek(@Param("fsrq")String fsrq,@Param("fund_code") String fundCode);

    @Select("select avg(dwjz) from (select dwjz from record where fsrq < #{fsrq} and fund_code = #{fund_code} order by fsrq desc limit 21) x")
    BigDecimal avgMonth(@Param("fsrq")String fsrq,@Param("fund_code") String fundCode);

    @Select("select avg(dwjz) from (select dwjz from record where fsrq < #{fsrq} and fund_code = #{fund_code} order by fsrq desc limit 63) x")
    BigDecimal avg3month(@Param("fsrq")String fsrq,@Param("fund_code") String fundCode);
}
