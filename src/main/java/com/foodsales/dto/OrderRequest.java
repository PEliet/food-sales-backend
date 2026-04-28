package com.foodsales.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private List<Integer> cartItemIds;
    private Integer addressId;
    private String remark;
    private Integer couponId;
}