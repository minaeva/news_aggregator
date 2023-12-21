package com.nfa.service;

import com.nfa.dto.StatisticsDto;
import com.nfa.dto.SubscriptionDto;
import com.nfa.entity.ReaderStatistics;
import com.nfa.repository.ReaderStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReaderStatisticsServiceImpl implements ReaderStatisticsService {

    private final ReaderStatisticsRepository readerStatisticsRepository;

    @Override
    @Transactional
    public StatisticsDto save(SubscriptionDto subscriptionDto) {
        ReaderStatistics readerStatisticsToSave = new ReaderStatistics(subscriptionDto.readerName(), subscriptionDto.readerEmail());
        ReaderStatistics savedReaderStatistics = readerStatisticsRepository.save(readerStatisticsToSave);
        return toDto(savedReaderStatistics);
    }

    private StatisticsDto toDto(ReaderStatistics entity) {
        return new StatisticsDto(entity.getName(), entity.getEmail(),
                entity.getDateCreated());
    }
}
