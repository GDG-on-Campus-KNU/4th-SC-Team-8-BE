package com.example.be.gemini.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GeminiResponse{
    private List<Candidate> candidates;

    public record Candidate(Content content){};

    public record Content(List<Parts> parts, String role){};

    public record Parts(String text){};
}
