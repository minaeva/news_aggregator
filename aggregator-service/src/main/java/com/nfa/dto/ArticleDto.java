package com.nfa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ArticleDto {
    String title;
    String description;
    String url;
    Date dateAdded;
    SourceDto sourceDto;
    List<KeywordDto> keywordDtos;

    public ArticleDto(String title, String description, String url, SourceDto sourceDto) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.sourceDto = sourceDto;
    }
}
