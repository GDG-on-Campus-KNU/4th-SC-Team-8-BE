package com.example.be.auth.dto;

public record GoogleAccountProfileResponse(String id,
                                           String email,
                                           String verified_email,
                                           String name,
                                           String given_name,
                                           String family_name,
                                           String picture,
                                           String locale) {
}
