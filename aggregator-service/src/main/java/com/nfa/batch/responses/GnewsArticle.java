package com.nfa.batch.responses;

import lombok.Data;

import java.util.Date;

@Data
public class GnewsArticle {

    private String title;
    private String description;
    private String content;
    private String url;
    private String image;
    private Date publishedAt;
    private GnewsSource source;
}
