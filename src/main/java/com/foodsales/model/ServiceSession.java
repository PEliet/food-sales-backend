package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("service_session")
public class ServiceSession {
    @TableId(type = IdType.AUTO)
    private Integer sessionId;
    private Integer userId;
    private Integer status;
    private String title;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}