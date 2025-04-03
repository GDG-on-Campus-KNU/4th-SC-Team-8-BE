package com.example.be.auth.dto;

public record GoogleAccessTokenResponse(String access_token,
                                        String expires_in,
                                        String scope,
                                        String token_type,
                                        String id_token) {
}
