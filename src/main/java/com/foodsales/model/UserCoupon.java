package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_coupon")
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    private Integer ucId;
    private Integer userId;
    private Integer couponId;
    private Integer status;
    private LocalDateTime usedTime;
    private Integer orderId;
    private LocalDateTime createTime;
}