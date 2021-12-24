package cn.blogscn.fund.model.domain;

import java.time.LocalDate;

public class Fund {
    private String fundCode;
    private String fundName;
    private LocalDate jzrq;// 净值日期

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public LocalDate getJzrq() {
        return jzrq;
    }

    public void setJzrq(LocalDate jzrq) {
        this.jzrq = jzrq;
    }
}
