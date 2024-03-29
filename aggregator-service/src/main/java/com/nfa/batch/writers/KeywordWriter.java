package com.nfa.batch.writers;

import com.nfa.dto.KeywordDto;
import com.nfa.entity.primary.Keyword;
import com.nfa.repository.primary.KeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class KeywordWriter implements ItemWriter<KeywordDto> {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private KeywordRepository keywordRepository;

    @Override
    public void write(Chunk<? extends KeywordDto> chunk) {
        log.info("Writing: {}", chunk.getItems().size());
        List<Keyword> newKeywords = new ArrayList<>();
        for (KeywordDto keywordDto : chunk.getItems()) {
            Optional<Keyword> savedKeyword = keywordRepository.findByNameIgnoreCase(keywordDto.name());
            if (savedKeyword.isEmpty()) {
                Keyword keywordToSave = new Keyword(keywordDto.name());
                newKeywords.add(keywordToSave);
            }
        }
        keywordRepository.saveAll(newKeywords);
    }
}
