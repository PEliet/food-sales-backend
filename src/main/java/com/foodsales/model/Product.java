package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer productId;
    private String name;
    private Integer categoryId;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stock;
    private Integer sales;
    private String image;
    private String images;
    private String description;
    private String spec;
    private String tags;
    private Integer isHot;
    private Integer isNew;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
