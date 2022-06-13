package cn.blogscn.fund.entity.etf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Indices {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String indexId;
    private String indexName;
    private BigDecimal indexIncreaseRt;
    private LocalDate lastDt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public BigDecimal getIndexIncreaseRt() {
        return indexIncreaseRt;
    }

    public void setIndexIncreaseRt(BigDecimal indexIncreaseRt) {
        this.indexIncreaseRt = indexIncreaseRt;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public LocalDate getLastDt() {
        return lastDt;
    }

    public void setLastDt(LocalDate lastDt) {
        this.lastDt = lastDt;
    }

    @Override
    public String toString() {
        return "Index{" +
                "id=" + id +
                ", indexName='" + indexName + '\'' +
                ", indexIcreaseRt=" + indexIncreaseRt +
                ", lastDt=" + lastDt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Indices index = (Indices) o;

        return getIndexName() != null ? getIndexName().equals(index.getIndexName())
                : index.getIndexName() == null;
    }

    @Override
    public int hashCode() {
        return getIndexName() != null ? getIndexName().hashCode() : 0;
    }
}
