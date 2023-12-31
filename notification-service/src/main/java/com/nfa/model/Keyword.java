package com.nfa.model;

import com.nfa.dto.SubscriptionDto;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@RedisHash("keyword")
public class Keyword implements Serializable {

    Integer id;
    private String name;
    private List<SubscriptionDto> subscriptionDtos;

    public Keyword(String name) {
        this.name = name;
    }
}
