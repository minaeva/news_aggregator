package com.nfa.service;

import com.nfa.dto.KeywordDto;
import com.nfa.entity.KeywordStatistics;
import com.nfa.repository.KeywordStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeywordStatisticsServiceImpl implements KeywordStatisticsService {

    private final KeywordStatisticsRepository keywordStatisticsRepository;

    @Override
    @Transactional
    public KeywordDto save(String keywordName) {
        KeywordStatistics keywordStatistics = new KeywordStatistics(keywordName);
        KeywordStatistics savedKeywordStatistics = keywordStatisticsRepository.save(keywordStatistics);
        return toDto(savedKeywordStatistics);
    }

    private KeywordDto toDto(KeywordStatistics entity) {
        return new KeywordDto(entity.getName(),
                entity.getDateCreated());
    }
}
