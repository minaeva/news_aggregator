package com.nfa.service;

import com.nfa.entity.primary.Article;

import java.util.Comparator;

public class ArticleDateComparator implements Comparator<Article> {

    @Override
    public int compare(Article a1, Article a2) {
        if (a1 == null || a1.getDateCreated() == null) {
            return (a2 == null || a2.getDateCreated() == null) ? 0 : 1;
        }
        if (a2 == null || a2.getDateCreated() == null) {
            return -1;
        }
        return a2.getDateCreated().compareTo(a1.getDateCreated());
    }
}
