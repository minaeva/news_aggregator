package com.nfa.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
public class SubscriptionRequest {

    @NotEmpty(message = "Keywords cannot be empty")
    private List<String> keywordNames;

    @NotNull(message = "Times per day cannot be null")
    private Integer timesPerDay;

}
