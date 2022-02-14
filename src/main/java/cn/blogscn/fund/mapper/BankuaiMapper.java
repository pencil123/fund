package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.Bankuai;
import cn.blogscn.fund.model.domain.FundRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface BankuaiMapper extends BaseMapper<Bankuai> {
    @Update("update gainian g set degree = (select degree from gn_record where gn_record.opendate = g.end_day and gn_record.code = g.code)")
    Boolean updateDegree();
    void batchInsert(@Param("bankuais") List<Bankuai> bankuais);

    @Update("update bankuai f\n"
            + "set count = (select count(1) \n"
            + "\tfrom bk_record r\n"
            + "\twhere r.code = f.code);")
    Boolean updateCount();
}
