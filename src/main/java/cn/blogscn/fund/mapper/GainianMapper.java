package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.Gainian;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface GainianMapper extends BaseMapper<Gainian> {
    @Update("update gainian g set degree = (select degree from gn_record where gn_record.opendate = g.end_day and gn_record.code = g.code)")
    Boolean updateDegree();

    @Update("update gainian f\n"
            + "set count = (select count(1) \n"
            + "\tfrom gn_record r\n"
            + "\twhere r.code = f.code);")
    Boolean updateCount();
}
