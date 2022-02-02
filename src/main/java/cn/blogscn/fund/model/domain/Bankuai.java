package cn.blogscn.fund.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Bankuai {
    @TableId
    private String code;
    private String name;
    private LocalDateTime createDatetime;
    private LocalDate startDay;
    private LocalDate endDay;

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

    public LocalDateTime getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(LocalDateTime createDatetime) {
        this.createDatetime = createDatetime;
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
}
