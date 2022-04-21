package cn.blogscn.fund.entity.indexFund;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDate;

public class IndexFundRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String code;
    private BigDecimal price;
    private BigDecimal degree;
    private BigDecimal ljjz;
    private BigDecimal jzzzl;
    private BigDecimal volume;
    private BigDecimal turnover;
    private LocalDate opendate;
    private BigDecimal avgWeek;
    private BigDecimal avgTwoWeek;
    private BigDecimal avgMonth;
    private BigDecimal recentWeekPrice;
    private BigDecimal recentTwoWeekPrice;
    private BigDecimal recentMonthPrice;
    private BigDecimal recentWeekRate;
    private BigDecimal recentTwoWeekRate;
    private BigDecimal recentMonthRate;

    public BigDecimal getRecentWeekPrice() {
        return recentWeekPrice;
    }

    public void setRecentWeekPrice(BigDecimal recentWeekPrice) {
        this.recentWeekPrice = recentWeekPrice;
    }

    public BigDecimal getRecentTwoWeekPrice() {
        return recentTwoWeekPrice;
    }

    public void setRecentTwoWeekPrice(BigDecimal recentTwoWeekPrice) {
        this.recentTwoWeekPrice = recentTwoWeekPrice;
    }

    public BigDecimal getRecentMonthPrice() {
        return recentMonthPrice;
    }

    public void setRecentMonthPrice(BigDecimal recentMonthPrice) {
        this.recentMonthPrice = recentMonthPrice;
    }

    public BigDecimal getRecentWeekRate() {
        return recentWeekRate;
    }

    public void setRecentWeekRate(BigDecimal recentWeekRate) {
        this.recentWeekRate = recentWeekRate;
    }

    public BigDecimal getRecentTwoWeekRate() {
        return recentTwoWeekRate;
    }

    public void setRecentTwoWeekRate(BigDecimal recentTwoWeekRate) {
        this.recentTwoWeekRate = recentTwoWeekRate;
    }

    public BigDecimal getRecentMonthRate() {
        return recentMonthRate;
    }

    public void setRecentMonthRate(BigDecimal recentMonthRate) {
        this.recentMonthRate = recentMonthRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDegree() {
        return degree;
    }

    public void setDegree(BigDecimal degree) {
        this.degree = degree;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public LocalDate getOpendate() {
        return opendate;
    }

    public void setOpendate(LocalDate opendate) {
        this.opendate = opendate;
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

    @Override
    public String toString() {
        return "IndexFundRecord{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", price=" + price +
                ", degree=" + degree +
                ", ljjz=" + ljjz +
                ", jzzzl=" + jzzzl +
                ", volume=" + volume +
                ", turnover=" + turnover +
                ", opendate=" + opendate +
                ", avgWeek=" + avgWeek +
                ", avgTwoWeek=" + avgTwoWeek +
                ", avgMonth=" + avgMonth +
                '}';
    }
}
