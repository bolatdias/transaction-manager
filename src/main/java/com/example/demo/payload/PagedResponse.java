package com.example.demo.payload;


import lombok.Data;

import java.util.List;

@Data
public class PagedResponse<T> {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    private List<T> content;
}
