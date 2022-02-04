package cn.blogscn.fund.service.impl;

import cn.blogscn.fund.mapper.BankuaiMapper;
import cn.blogscn.fund.model.domain.Bankuai;
import cn.blogscn.fund.model.domain.BkRecord;
import cn.blogscn.fund.service.BankuaiService;
import cn.blogscn.fund.service.BkRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankuaiServiceImpl extends ServiceImpl<BankuaiMapper, Bankuai> implements BankuaiService {
    @Autowired
    private BkRecordService bkRecordService;

    public Boolean updateStartAndEndDay(){
        List<Bankuai> bankuais = list();
        QueryWrapper<BkRecord> bkRecordQueryWrapperAsc = new QueryWrapper<>();
        QueryWrapper<BkRecord> bkRecordQueryWrapperDesc = new QueryWrapper<>();
        for(Bankuai bankuai:bankuais){
            // startDay Asc
            bkRecordQueryWrapperAsc.select("opendate");
            bkRecordQueryWrapperAsc.orderByAsc("opendate");
            bkRecordQueryWrapperAsc.last("limit 1");
            bkRecordQueryWrapperAsc.eq("code",bankuai.getCode());
            BkRecord startDayOne = bkRecordService.getOne(bkRecordQueryWrapperAsc);
            bkRecordQueryWrapperAsc.clear();
            // endDay Desc
            bkRecordQueryWrapperDesc.select("opendate");
            bkRecordQueryWrapperDesc.orderByDesc("opendate");
            bkRecordQueryWrapperDesc.last("limit 1");
            bkRecordQueryWrapperDesc.eq("code",bankuai.getCode());
            BkRecord endDayOne = bkRecordService.getOne(bkRecordQueryWrapperDesc);
            bkRecordQueryWrapperDesc.clear();
            bankuai.setStartDay(startDayOne.getOpendate());
            bankuai.setEndDay(endDayOne.getOpendate());
            bankuai.setDegree(bkRecordService.calculateDegree(bankuai.getCode(),endDayOne.getOpendate()));
            updateById(bankuai);
        }
        return true;
    }

    @Override
    public List<Bankuai> listByDegreeDesc() {
        QueryWrapper<Bankuai> bankuaiQueryWrapper = new QueryWrapper<>();
        bankuaiQueryWrapper.orderByDesc("degree");
        return list(bankuaiQueryWrapper);
    }
}
