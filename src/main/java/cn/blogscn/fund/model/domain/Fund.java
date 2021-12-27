package cn.blogscn.fund.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Fund {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String fundCode;
    private BigDecimal dwjz;
    private BigDecimal ljjz;
    private BigDecimal jzzzl;
    private LocalDate fsrq;
    private BigDecimal avgWeek;
    private BigDecimal avgMonth;
    private BigDecimal avg3month;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public BigDecimal getDwjz() {
        return dwjz;
    }

    public void setDwjz(BigDecimal dwjz) {
        this.dwjz = dwjz;
    }

    public BigDecimal getLjjz() {
        return ljjz;
    }

    public void setLjjz(BigDecimal ljjz) {
        this.ljjz = ljjz;
    }

    public BigDecimal getJzzzl() {
        return jzzzl;
    }

    public void setJzzzl(BigDecimal jzzzl) {
        this.jzzzl = jzzzl;
    }

    public LocalDate getFsrq() {
        return fsrq;
    }

    public void setFsrq(LocalDate fsrq) {
        this.fsrq = fsrq;
    }

    public BigDecimal getAvgWeek() {
        return avgWeek;
    }

    public void setAvgWeek(BigDecimal avgWeek) {
        this.avgWeek = avgWeek;
    }

    public BigDecimal getAvgMonth() {
        return avgMonth;
    }

    public void setAvgMonth(BigDecimal avgMonth) {
        this.avgMonth = avgMonth;
    }

    public BigDecimal getAvg3month() {
        return avg3month;
    }

    public void setAvg3month(BigDecimal avg3month) {
        this.avg3month = avg3month;
    }
}
