package cn.blogscn.fund.mapper;

import cn.blogscn.fund.model.domain.Indices;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicesMapper extends BaseMapper<Indices> {
    @Update("update indices g set degree = (select degree from index_record where index_record.opendate = g.end_day and index_record.code = g.code)")
    Boolean updateDegree();
}
