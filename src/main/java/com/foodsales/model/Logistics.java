package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("logistics")
public class Logistics {
    @TableId(type = IdType.AUTO)
    private Integer logisticsId;
    private Integer orderId;
    private String company;
    private String trackingNo;
    private String status;
    private String remark;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
