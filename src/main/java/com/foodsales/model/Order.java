package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private String orderNo;
    private Integer userId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal payAmount;
    private String status;
    private String payType;
    private LocalDateTime payTime;
    private Integer addressId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
    private String cancelReason;
    private String refundReason;
    private LocalDateTime closeTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
