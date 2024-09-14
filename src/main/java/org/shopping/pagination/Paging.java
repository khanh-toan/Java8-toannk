package org.shopping.pagination;

import lombok.Data;

import java.util.List;

@Data
public class Paging<T> {
    private List<T> data;
    private int totalItems;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public Paging(List<T> data, int totalItems, int currentPage, int pageSize) {
        this.data = data;
        this.totalItems = totalItems;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }
}