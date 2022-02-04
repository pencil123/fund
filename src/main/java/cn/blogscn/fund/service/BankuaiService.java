package cn.blogscn.fund.service;

import cn.blogscn.fund.model.domain.Bankuai;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BankuaiService extends IService<Bankuai> {
    Boolean updateStartAndEndDay();
}
