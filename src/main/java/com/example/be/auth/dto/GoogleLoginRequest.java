package com.example.be.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GoogleLoginRequest(@Schema(description = "구글로부터 받은 code", example = "4%2F0AfJohXlpgkCgIruONFGdaZ9NBbqWY77MkcJlPLFJg9NQmB38ZFpy80qUjKCFOWWSRIGevA") String code) {
}
