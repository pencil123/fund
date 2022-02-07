package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Gainian;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface GainianService extends IService<Gainian> {
    Boolean  updateDegree();
    Boolean updateStartAndEndDay();
    List<Gainian> listByDegreeDesc();
}
