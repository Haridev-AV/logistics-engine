package com.routeoptima.logistics_engine.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}