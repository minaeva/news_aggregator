package com.nfa.client.responses;

import lombok.Data;

import java.util.List;

@Data
public class BBCResponse {

    private String status;
    private int totalResults;
    private List<BBCArticle> articles;
}
