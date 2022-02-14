package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Bankuai;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface BankuaiService extends IService<Bankuai> {
    Boolean  updateDegree();
    Boolean updateStartAndEndDay();
    List<Bankuai> listByDegreeDesc();
    Boolean batchInsert(List<Bankuai> bankuaiList);
}
