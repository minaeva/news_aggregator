package com.nfa.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDto(String title, String description, String content, String url,
                         LocalDateTime dateCreated, SourceDto sourceDto, List<KeywordDto> keywordDtos) {

}
