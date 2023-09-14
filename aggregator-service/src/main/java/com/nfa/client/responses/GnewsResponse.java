package com.nfa.client.responses;

import lombok.Data;

import java.util.List;

@Data
public class GnewsResponse {

    private int totalArticles;
    private List<GnewsArticle> articles;
}
