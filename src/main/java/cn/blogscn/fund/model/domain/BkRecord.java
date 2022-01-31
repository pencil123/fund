package cn.blogscn.fund.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BkRecord {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String bkCode;
    private BigDecimal price;
    private LocalDate opendate;
    private BigDecimal turnover;
    private BigDecimal netamount;
    private BigDecimal r0Net;
    private BigDecimal r0Ratio;
    private BigDecimal r0xRatio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBkCode() {
        return bkCode;
    }

    public void setBkCode(String bkCode) {
        this.bkCode = bkCode;
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
}
