package fpt.project.bsmart.entity.common;

import java.io.Serializable;
import java.util.List;

public class ApiPage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer totalPages;

    private Long totalItems;

    private Integer currentPage;

    private Boolean first;

    private Boolean last;

    private Integer pageItemSize;

    private Integer pageSize;

    private List<T> items;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getPageItemSize() {
        return pageItemSize;
    }

    public void setPageItemSize(Integer pageItemSize) {
        this.pageItemSize = pageItemSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
