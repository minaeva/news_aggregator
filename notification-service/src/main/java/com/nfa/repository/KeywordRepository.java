package com.nfa.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@AllArgsConstructor
public class KeywordRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY = "keywords";

    public void addKeyword(String keyword) {
        redisTemplate.opsForSet().add(KEY, keyword);
    }

    public Set<String> getAllKeywords() {
        return redisTemplate.opsForSet().members(KEY);
    }

    public void deleteKeyword(String keyword) {
        redisTemplate.opsForSet().remove(KEY, keyword);
    }
}
