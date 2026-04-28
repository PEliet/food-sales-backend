package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Integer messageId;
    private Integer userId;
    private String title;
    private String content;
    private Integer type;
    private Integer isRead;
    private Integer relatedId;
    private LocalDateTime createTime;
}
