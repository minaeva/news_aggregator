package com.nfa.client.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GnewsArticle {

    private String title;
    private String description;
    private String content;
    private String url;
    private String image;
    private LocalDateTime publishedAt;
    private GnewsSource source;
}
