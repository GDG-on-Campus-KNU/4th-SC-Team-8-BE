package com.example.be.auth.dto;

public record GoogleAccessTokenRequest(String code,
                                       String client_id,
                                       String client_secret,
                                       String redirect_uri,
                                       String grant_type) {
}
