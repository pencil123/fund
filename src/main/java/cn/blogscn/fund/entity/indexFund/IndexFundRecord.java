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
