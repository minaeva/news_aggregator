package com.nfa.client.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BBCArticle {

    private BBCSource source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private LocalDateTime publishedAt;
    private String content;
}
