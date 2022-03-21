package cn.blogscn.fund.service.index.impl;

import cn.blogscn.fund.entity.index.IndexRecord;
import cn.blogscn.fund.entity.index.Indices;
import cn.blogscn.fund.mapper.index.IndicesMapper;
import cn.blogscn.fund.service.index.IndexRecordService;
import cn.blogscn.fund.service.index.IndicesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IndicesServiceImpl extends ServiceImpl<IndicesMapper, Indices> implements
        IndicesService {

    @Autowired
    private IndexRecordService indexRecordService;

    @Override
    public Boolean updateDegree() {
        return baseMapper.updateDegree();
    }

    @Override
    public Boolean updateStartAndEndDay() {
        List<Indices> indicesList = list();
        QueryWrapper<IndexRecord> indexRecordQueryWrapperAsc = new QueryWrapper<>();

        QueryWrapper<IndexRecord> indexRecordQueryWrapperDesc = new QueryWrapper<>();

        for (Indices indices : indicesList) {
            // startDay
            indexRecordQueryWrapperAsc.select("opendate");
            indexRecordQueryWrapperAsc.orderByAsc("opendate");
            indexRecordQueryWrapperAsc.last("limit 1");
            indexRecordQueryWrapperAsc.eq("code", indices.getCode());
            IndexRecord startDayOne = indexRecordService.getOne(indexRecordQueryWrapperAsc);
            indexRecordQueryWrapperAsc.clear();
            //endDay
            indexRecordQueryWrapperDesc.select("opendate");
            indexRecordQueryWrapperDesc.orderByDesc("opendate");
            indexRecordQueryWrapperDesc.last("limit 1");
            indexRecordQueryWrapperDesc.eq("code", indices.getCode());
            IndexRecord endDayOne = indexRecordService.getOne(indexRecordQueryWrapperDesc);
            indexRecordQueryWrapperDesc.clear();
            indices.setStartDay(startDayOne.getOpendate());
            indices.setEndDay(endDayOne.getOpendate());
            indices.setDegree(
                    indexRecordService.calculateDegree(indices.getCode(), endDayOne.getOpendate()));
            updateById(indices);
        }
        return true;
    }

    @Override
    public List<Indices> listByDegreeDesc() {
        QueryWrapper<Indices> indicesQueryWrapper = new QueryWrapper<>();
        indicesQueryWrapper.orderByDesc("degree");
        return list(indicesQueryWrapper);
    }
}
