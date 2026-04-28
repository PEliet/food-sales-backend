package com.foodsales.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoginResponse {
    private String token;
    private Integer userId;
    private String username;
    private String nickname;
    private String avatar;
    private Integer role;
    private String phone;
    private BigDecimal balance;
    private Integer points;
}