package cn.blogscn.fund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BkRecordDto {

    private String code;
    private String name;
    private BigDecimal price;
    private BigDecimal degree;
    private LocalDate opendate;
    private BigDecimal turnover;
    private BigDecimal netamount;
    private BigDecimal r0Net;
    private BigDecimal r0Ratio;
    private BigDecimal r0xRatio;
    private BigDecimal avgWeek;
    private BigDecimal avgTwoWeek;
    private BigDecimal avgMonth;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDegree() {
        return degree;
    }

    public void setDegree(BigDecimal degree) {
        this.degree = degree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getOpendate() {
        return opendate;
    }

    public void setOpendate(LocalDate opendate) {
        this.opendate = opendate;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public BigDecimal getNetamount() {
        return netamount;
    }

    public void setNetamount(BigDecimal netamount) {
        this.netamount = netamount;
    }

    public BigDecimal getR0Net() {
        return r0Net;
    }

    public void setR0Net(BigDecimal r0Net) {
        this.r0Net = r0Net;
    }

    public BigDecimal getR0Ratio() {
        return r0Ratio;
    }

    public void setR0Ratio(BigDecimal r0Ratio) {
        this.r0Ratio = r0Ratio;
    }

    public BigDecimal getR0xRatio() {
        return r0xRatio;
    }

    public void setR0xRatio(BigDecimal r0xRatio) {
        this.r0xRatio = r0xRatio;
    }

    public BigDecimal getAvgWeek() {
        return avgWeek;
    }

    public void setAvgWeek(BigDecimal avgWeek) {
        this.avgWeek = avgWeek;
    }

    public BigDecimal getAvgTwoWeek() {
        return avgTwoWeek;
    }

    public void setAvgTwoWeek(BigDecimal avgTwoWeek) {
        this.avgTwoWeek = avgTwoWeek;
    }

    public BigDecimal getAvgMonth() {
        return avgMonth;
    }

    public void setAvgMonth(BigDecimal avgMonth) {
        this.avgMonth = avgMonth;
    }
}
