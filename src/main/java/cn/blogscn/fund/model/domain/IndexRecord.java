package cn.blogscn.fund.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDate;

public class IndexRecord {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String code;
    private BigDecimal price;
    private Long volume;
    private BigDecimal turnover;
    private LocalDate opendate;
    private BigDecimal avgWeek;
    private BigDecimal avgTwoWeek;
    private BigDecimal avgMonth;
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

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
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
        return "IndexRecord{" +
                "code='" + code + '\'' +
                ", price=" + price +
                ", volume=" + volume +
                ", turnover=" + turnover +
                ", opendate=" + opendate +
                '}';
    }
}
