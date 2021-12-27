package cn.blogscn.fund.model.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Fund {
    private String fundCode;
    private BigDecimal dwjz;
    private BigDecimal ljjz;
    private BigDecimal jzzzl;
    private LocalDate fsrq;

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
}
