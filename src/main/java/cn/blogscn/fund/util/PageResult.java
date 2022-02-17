package cn.blogscn.fund.util;


import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;
import java.util.List;

/**
 * <功能描述>分页Result
 *
 * @author 20102225
 * @date 2020/9/17 14:53
 */
public class PageResult<T> implements Serializable {

    private Long totalCount;
    private List<T> records;
    private Long pageSize;
    private Long totalPage;
    private Long currentPage;

    public PageResult(IPage<T> t) {
        this.currentPage = t.getCurrent();
        this.pageSize = t.getSize();
        this.totalCount = t.getTotal();
        this.totalPage = t.getPages();
        this.records = t.getRecords();
    }

    public PageResult() {
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }


}
