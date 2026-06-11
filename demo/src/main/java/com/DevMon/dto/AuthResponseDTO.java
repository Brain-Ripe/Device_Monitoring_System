package com.DevMon.dto;

public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    // Getter and Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}