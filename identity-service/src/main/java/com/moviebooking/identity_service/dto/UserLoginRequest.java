package com.moviebooking.identity_service.dto;
import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}