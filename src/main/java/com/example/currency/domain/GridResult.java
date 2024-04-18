package com.example.currency.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GridResult<T> {
    private long totalSize;
    private List<T> data;
    private int pageNumber;
    private int pageSize;


    public GridResult(List<T> data, int pageNumber, int pageSize, long totalSize) {
        this.totalSize = totalSize;
        this.data = data;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
