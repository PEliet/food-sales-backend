package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("banner")
public class Banner {
    @TableId(type = IdType.AUTO)
    private Integer bannerId;
    private String title;
    private String image;
    private String link;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
}