package com.nfa.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDto(String title, String description, String url,
                         LocalDateTime dateAdded, SourceDto sourceDto, List<KeywordDto> keywordDtos) {

}
