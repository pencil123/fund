package cn.blogscn.fund.dto;

import java.math.BigDecimal;

public class IndexWithRateDto {
    private String weekCode;
    private String weekName;
    private BigDecimal weekRate;
    private String twoWeekCode;
    private String twoWeekName;
    private BigDecimal twoWeekRate;
    private String monthCode;
    private String monthName;
    private BigDecimal monthRate;

    public String getWeekCode() {
        return weekCode;
    }

    public void setWeekCode(String weekCode) {
        this.weekCode = weekCode;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public BigDecimal getWeekRate() {
        return weekRate;
    }

    public void setWeekRate(BigDecimal weekRate) {
        this.weekRate = weekRate;
    }

    public String getTwoWeekCode() {
        return twoWeekCode;
    }

    public void setTwoWeekCode(String twoWeekCode) {
        this.twoWeekCode = twoWeekCode;
    }

    public String getTwoWeekName() {
        return twoWeekName;
    }

    public void setTwoWeekName(String twoWeekName) {
        this.twoWeekName = twoWeekName;
    }

    public BigDecimal getTwoWeekRate() {
        return twoWeekRate;
    }

    public void setTwoWeekRate(BigDecimal twoWeekRate) {
        this.twoWeekRate = twoWeekRate;
    }

    public String getMonthCode() {
        return monthCode;
    }

    public void setMonthCode(String monthCode) {
        this.monthCode = monthCode;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public BigDecimal getMonthRate() {
        return monthRate;
    }

    public void setMonthRate(BigDecimal monthRate) {
        this.monthRate = monthRate;
    }
}
