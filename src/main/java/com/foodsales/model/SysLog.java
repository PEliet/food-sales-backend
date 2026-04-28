package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_log")
public class SysLog {
    @TableId(type = IdType.AUTO)
    private Long logId;
    private Integer userId;
    private String username;
    private String module;
    private String action;
    private String content;
    private String ip;
    private LocalDateTime createTime;
}