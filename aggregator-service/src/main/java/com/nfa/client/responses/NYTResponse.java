package com.nfa.client.responses;

import lombok.Data;

import java.util.List;

@Data
public class NYTResponse {

    private int num_results;
    private List<NYTArticle> results;
}
