package com.nfa.client.responses;

import lombok.Data;

import java.util.Date;

@Data
public class BBCArticle {

    private BBCSource source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private Date publishedAt;
    private String content;
}
