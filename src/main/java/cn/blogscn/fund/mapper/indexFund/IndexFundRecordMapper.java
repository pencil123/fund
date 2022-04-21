package cn.blogscn.fund.mapper.indexFund;

import cn.blogscn.fund.entity.indexFund.IndexFundRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexFundRecordMapper extends BaseMapper<IndexFundRecord> {

    //select avg(price) from (select price from fund where opendate < ${opendate} and code = ${code} order by opendate desc limit 7) x;
    @Select("select avg(price) from (select price from index_fund_record where opendate <= #{opendate} and code = #{code} order by opendate desc limit 5) x")
    BigDecimal avgWeek(@Param("opendate") String opendate, @Param("code") String fundCode);

    @Select("select price from index_fund_record where code = #{code} and  opendate <= #{opendate} order by opendate desc  limit 5,1")
    BigDecimal recentWeekPrice(@Param("opendate") String opendate, @Param("code") String code);

    @Select("select price from index_fund_record where code = #{code} and  opendate <= #{opendate} order by opendate desc  limit 10,1")
    BigDecimal recentTwoWeekPrice(@Param("opendate") String opendate, @Param("code") String code);

    @Select("select price from index_fund_record where code = #{code} and  opendate <= #{opendate} order by opendate desc  limit 20,1")
    BigDecimal recentMonthPrice(@Param("opendate") String opendate, @Param("code") String code);

    @Select("select avg(price) from (select price from index_fund_record where opendate <= #{opendate} and code = #{code} order by opendate desc limit 20) x")
    BigDecimal avgMonth(@Param("opendate") String opendate, @Param("code") String fundCode);

    @Select("select avg(price) from (select price from index_fund_record where opendate <= #{opendate} and code = #{code} order by opendate desc limit 10) x")
    BigDecimal avgTwoWeek(@Param("opendate") String opendate, @Param("code") String fundCode);

    @Select("select (price-avg_week)*20+(price-avg_two_week)*10+(price-avg_month)*5 from index_fund_record where code = #{code} and opendate = #{opendate}")
    BigDecimal calculateDegree(@Param("opendate") String opendate, @Param("code") String code);

    @Update("update index_fund_record set degree = ((price-avg_week)*20+(price-avg_two_week)*10+(price-avg_month)*5)/price where degree is null")
    Boolean updateDegree();

    @Update("UPDATE\n"
            + "        index_fund_record\n"
            + "SET\n"
            + "        recent_week_rate = (price-recent_week_price)/price *100,\n"
            + "\t\trecent_two_week_rate = (price-recent_two_week_price)/price*100,\n"
            + "\t\trecent_month_rate=(price-recent_month_price)/price*100\n"
            + "WHERE\n"
            + "        recent_week_rate IS NULL or recent_two_week_rate is null or recent_month_rate is null;")
    Boolean updatePriceRate();



    boolean batchInsertOrUpdateJz(
            @Param("indexFundRecordList") List<IndexFundRecord> indexFundRecordList);
}
