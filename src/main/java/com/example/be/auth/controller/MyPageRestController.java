package com.example.be.auth.controller;

import com.example.be.auth.dto.PasswordRequest;
import com.example.be.auth.dto.UserInfoResponse;
import com.example.be.auth.service.CustomUserDetails;
import com.example.be.auth.service.MyPageService;
import com.example.be.common.annotation.ApiErrorCodeExamples;
import com.example.be.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mypage")
@Tag(name = "마이페이지 기능", description = "비밀번호 변경, 로그아웃, 회원 탈퇴 기능을 포함한다.")
public class MyPageRestController {
    private final MyPageService myPageService;

    @Operation(summary = "회원 탈퇴 기능",
            description = "사용자의 정보를 제거하고 회원탈퇴를 수행한다.")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        myPageService.withdrawal(userDetails.getUser());

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "로그아웃 기능",
            description = "로그인한 사용자의 인증 권한을 제거한다.")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        myPageService.logout(userDetails.getUser());

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "비밀번호 변경 기능",
            description = "로그인한 사용자의 비밀 번호를 변경한다.")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody PasswordRequest passwordRequest) {
        myPageService.changePassword(userDetails.getUser(), passwordRequest);

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "토큰 유효성 검사 기능",
            description = "헤더에 있는 access Token의 유효성을 검사한다.")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @PostMapping("/token")
    public ResponseEntity<?> chkValidToken(HttpServletRequest request){
        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "사용자 정보 조회 기능",
            description = "사용자의 이름을 조회한다.(필요 시 이메일도 추가 가능합니다!)")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(myPageService.getUserInfo(userDetails.getUser()));
    }
}
