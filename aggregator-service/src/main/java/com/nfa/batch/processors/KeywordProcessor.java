package com.nfa.batch.processors;

import com.nfa.dto.KeywordDto;
import com.nfa.entity.secondary.SecondaryKeyword;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class KeywordProcessor implements ItemProcessor<SecondaryKeyword, KeywordDto> {

    @Override
    public KeywordDto process(SecondaryKeyword item) {
        System.out.println("Processing SecondaryKeyword " + item);
        return new KeywordDto(item.getName());
    }
}
