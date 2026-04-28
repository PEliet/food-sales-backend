package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("service_message")
public class ServiceMessage {
    @TableId(type = IdType.AUTO)
    private Integer msgId;
    private Integer sessionId;
    private Integer senderType;
    private String content;
    private LocalDateTime createTime;
}