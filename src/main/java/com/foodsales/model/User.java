package com.foodsales.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String userNo;
    private String username;
    private String password;
    private String nickname;
    private Integer role;
    private Integer gender;
    private String phone;
    private String email;
    private String avatar;
    private String openid;
    private BigDecimal balance;
    private Integer points;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
