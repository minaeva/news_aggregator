package com.nfa.client.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NYTArticle {

    private String url;
    private String source;
    private String adx_keywords;
    private String type;
    private String title;
    @JsonProperty(value = "abstract")
    private String content;
    @JsonProperty(value = "published_date")
    private LocalDate publishedDate;
}
