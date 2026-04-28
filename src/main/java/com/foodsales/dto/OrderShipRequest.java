package com.foodsales.dto;

import lombok.Data;

@Data
public class OrderShipRequest {
    private Integer orderId;
    private String company;
    private String trackingNo;
}