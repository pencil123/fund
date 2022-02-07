package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.Bankuai;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface BankuaiMapper extends BaseMapper<Bankuai> {
    @Update("update gainian g set degree = (select degree from gn_record where gn_record.opendate = g.end_day and gn_record.code = g.code)")
    Boolean updateDegree();
}
