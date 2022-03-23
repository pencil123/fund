package cn.blogscn.fund.entity.indexFund;

import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class IndexFund {

    @TableId
    private String code;
    private String name;
    private BigDecimal degree;
    private LocalDateTime createTime;
    private LocalDate startDay;
    private LocalDate endDay;
    private Integer count;
    private Integer expect;
    private Boolean backward;
    private Boolean stopped;
    private Boolean disabled;

    public Boolean getStopped() {
        return stopped;
    }

    public void setStopped(Boolean stopped) {
        this.stopped = stopped;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getBackward() {
        return backward;
    }

    public void setBackward(Boolean backward) {
        this.backward = backward;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDegree() {
        return degree;
    }

    public void setDegree(BigDecimal degree) {
        this.degree = degree;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

    public Integer getExpect() {
        return expect;
    }

    public void setExpect(Integer expect) {
        this.expect = expect;
    }

    @Override
    public String toString() {
        return "IndexFund{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", degree=" + degree +
                ", createTime=" + createTime +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                '}';
    }
}
