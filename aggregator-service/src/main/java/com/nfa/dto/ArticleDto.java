package com.nfa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ArticleDto {
    String title;
    String description;
    String url;
    LocalDateTime dateAdded;
    SourceDto sourceDto;
    List<KeywordDto> keywordDtos;

    public ArticleDto(String title, String description, String url, SourceDto sourceDto) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.sourceDto = sourceDto;
    }
}
