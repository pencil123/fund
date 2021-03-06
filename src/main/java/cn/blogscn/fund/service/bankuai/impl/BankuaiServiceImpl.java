package cn.blogscn.fund.service.bankuai.impl;

import cn.blogscn.fund.entity.bankuai.Bankuai;
import cn.blogscn.fund.entity.bankuai.BkRecord;
import cn.blogscn.fund.mapper.bankuai.BankuaiMapper;
import cn.blogscn.fund.service.bankuai.BankuaiService;
import cn.blogscn.fund.service.bankuai.BkRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankuaiServiceImpl extends ServiceImpl<BankuaiMapper, Bankuai> implements
        BankuaiService {

    @Autowired
    private BkRecordService bkRecordService;

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }

    public Boolean updateStartAndEndDay() {
        List<Bankuai> bankuais = list();
        QueryWrapper<BkRecord> bkRecordQueryWrapperAsc = new QueryWrapper<>();
        QueryWrapper<BkRecord> bkRecordQueryWrapperDesc = new QueryWrapper<>();
        for (Bankuai bankuai : bankuais) {
            // startDay Asc
            bkRecordQueryWrapperAsc.select("opendate");
            bkRecordQueryWrapperAsc.orderByAsc("opendate");
            bkRecordQueryWrapperAsc.last("limit 1");
            bkRecordQueryWrapperAsc.eq("code", bankuai.getCode());
            BkRecord startDayOne = bkRecordService.getOne(bkRecordQueryWrapperAsc);
            bkRecordQueryWrapperAsc.clear();
            // endDay Desc
            bkRecordQueryWrapperDesc.select("opendate");
            bkRecordQueryWrapperDesc.orderByDesc("opendate");
            bkRecordQueryWrapperDesc.last("limit 1");
            bkRecordQueryWrapperDesc.eq("code", bankuai.getCode());
            BkRecord endDayOne = bkRecordService.getOne(bkRecordQueryWrapperDesc);
            bkRecordQueryWrapperDesc.clear();
            bankuai.setStartDay(startDayOne.getOpendate());
            bankuai.setEndDay(endDayOne.getOpendate());
            bankuai.setDegree(
                    bkRecordService.calculateDegree(bankuai.getCode(), endDayOne.getOpendate()));
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

    @Override
    public Boolean batchInsert(List<Bankuai> bankuaiList) {
        baseMapper.batchInsert(bankuaiList);
        return true;
    }
}
