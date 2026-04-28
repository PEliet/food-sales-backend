package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("inventory")
public class Inventory {
    @TableId(type = IdType.AUTO)
    private Integer inventoryId;
    private Integer productId;
    private Integer quantity;
    private Integer warning;
    private LocalDateTime updateTime;
}
