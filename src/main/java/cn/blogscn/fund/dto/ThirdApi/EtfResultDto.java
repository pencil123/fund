package cn.blogscn.fund.dto.ThirdApi;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EtfResultDto {
    private Integer code;
    private String name;
    private String issuer;
    private Integer amount; // 份额（万份）
    private BigDecimal unitTotal; //规模（亿元）
    private BigDecimal unitIncr; // 规模变化（亿元）
    private BigDecimal price; // 现价
    private BigDecimal volume; // 成交额
    private LocalDate date;
    private BigDecimal increaseRt; // 涨幅
    private BigDecimal estimateValue; // 估值
    private BigDecimal discountRt; // 溢价率
    private BigDecimal fundNav; //净值
    private LocalDate navDate; // 净值日期
    private String indexName; //跟踪的指数名称
    private BigDecimal indexIcreaseRt; // 指数涨幅

    public BigDecimal getDiscountRt() {
        return discountRt;
    }

    public void setDiscountRt(BigDecimal discountRt) {
        this.discountRt = discountRt;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getUnitTotal() {
        return unitTotal;
    }

    public void setUnitTotal(BigDecimal unitTotal) {
        this.unitTotal = unitTotal;
    }

    public BigDecimal getUnitIncr() {
        return unitIncr;
    }

    public void setUnitIncr(BigDecimal unitIncr) {
        this.unitIncr = unitIncr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getIncreaseRt() {
        return increaseRt;
    }

    public void setIncreaseRt(BigDecimal increaseRt) {
        this.increaseRt = increaseRt;
    }

    public BigDecimal getEstimateValue() {
        return estimateValue;
    }

    public void setEstimateValue(BigDecimal estimateValue) {
        this.estimateValue = estimateValue;
    }

    public BigDecimal getFundNav() {
        return fundNav;
    }

    public void setFundNav(BigDecimal fundNav) {
        this.fundNav = fundNav;
    }

    public LocalDate getNavDate() {
        return navDate;
    }

    public void setNavDate(LocalDate navDate) {
        this.navDate = navDate;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public BigDecimal getIndexIcreaseRt() {
        return indexIcreaseRt;
    }

    public void setIndexIcreaseRt(BigDecimal indexIcreaseRt) {
        this.indexIcreaseRt = indexIcreaseRt;
    }

    @Override
    public String toString() {
        return "EtfResultDto{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", issuer='" + issuer + '\'' +
                ", amount=" + amount +
                ", unitTotal=" + unitTotal +
                ", unitIncr=" + unitIncr +
                ", price=" + price +
                ", volume=" + volume +
                ", date=" + date +
                ", increaseRt=" + increaseRt +
                ", estimateValue=" + estimateValue +
                ", discountRt=" + discountRt +
                ", fundNav=" + fundNav +
                ", navDate=" + navDate +
                ", indexName='" + indexName + '\'' +
                ", indexIcreaseRt=" + indexIcreaseRt +
                '}';
    }
}
