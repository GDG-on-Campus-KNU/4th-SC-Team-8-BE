package com.example.be.gemini.controller;

import com.example.be.common.annotation.ApiErrorCodeExamples;
import com.example.be.common.exception.ErrorCode;
import com.example.be.gemini.dto.AiRequest;
import com.example.be.gemini.dto.AiResponse;
import com.example.be.gemini.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
@Tag(name = "gemini 기능", description = "gemini에 질문을 보내 답변을 받는 기능을 포함한다.")
public class GeminiRestController {
    private final GeminiService geminiService;

    @Operation(summary = "gemini 질문 기능",
            description = "텍스트를 입력값으로 넣어 답변을 받는다.")
    @ApiErrorCodeExamples({ErrorCode.UNAUTHORIZED, ErrorCode.GEMINI_API_ERROR})
    @PostMapping()
    public ResponseEntity<AiResponse> getGeminiResponse(@RequestBody AiRequest request){
        return ResponseEntity.ok(geminiService.getGeminiResposne(request.text()));
    }
}
