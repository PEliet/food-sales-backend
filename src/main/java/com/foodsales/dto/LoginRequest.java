package com.foodsales.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String code;
    private Integer loginType;
}