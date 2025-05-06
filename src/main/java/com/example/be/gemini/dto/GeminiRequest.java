package com.example.be.gemini.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String text){
        this.contents = new ArrayList<>();
        Content content = new Content(text);
        contents.add(content);
    }

    @Getter
    public class Content{
        private List<Part> parts;

        public Content(String text){
            parts = new ArrayList<>();
            Part part = new Part(text);
            parts.add(part);
        }

        public record Part(String text){}
    }
}
