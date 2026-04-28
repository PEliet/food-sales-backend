package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer cartId;
    private Integer userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
