package com.example.be.gemini.service;

import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.ErrorException;
import com.example.be.gemini.dto.AiResponse;
import com.example.be.gemini.dto.GeminiRequest;
import com.example.be.gemini.dto.GeminiResponse;
import com.example.be.gemini.properties.GeminiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableConfigurationProperties(GeminiProperties.class)
@RequiredArgsConstructor
public class GeminiService {
    private final RestTemplate restTemplate;
    private final GeminiProperties geminiProperties;
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    public AiResponse getGeminiResposne(String text){
        String geminiURL = GEMINI_URL
                + geminiProperties.key();
        GeminiRequest request = new GeminiRequest(text);
        String description = "";
        try{
            GeminiResponse response = restTemplate.postForObject(geminiURL, request, GeminiResponse.class);
            description = response.getCandidates().getFirst().content().parts().getFirst().text();

            return new AiResponse(description);
        }catch (Exception e){
            throw new ErrorException(ErrorCode.GEMINI_API_ERROR);
        }
    }
}
