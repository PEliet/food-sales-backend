package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("logistics_track")
public class LogisticsTrack {
    @TableId(type = IdType.AUTO)
    private Integer trackId;
    private Integer logisticsId;
    private String operation;
    private String operator;
    private String location;
    private LocalDateTime createTime;
}
