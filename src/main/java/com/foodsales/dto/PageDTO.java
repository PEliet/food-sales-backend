package com.foodsales.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageDTO<T> {
    private long total;
    private int page;
    private int pageSize;
    private List<T> list;

    public static <T> PageDTO<T> of(long total, int page, int pageSize, List<T> list) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setTotal(total);
        dto.setPage(page);
        dto.setPageSize(pageSize);
        dto.setList(list);
        return dto;
    }
}