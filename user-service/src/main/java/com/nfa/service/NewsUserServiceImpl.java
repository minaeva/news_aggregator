package com.nfa.service;

import com.nfa.dto.NewsUserDto;
import com.nfa.entity.NewsUserEntity;
import com.nfa.exception.NewsUserNotFoundException;
import com.nfa.repository.NewsUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsUserServiceImpl implements NewsUserService {

    private final NewsUserRepository newsUserRepository;

    @Override
    public NewsUserDto findByEmail(String email) throws NewsUserNotFoundException {
        NewsUserEntity newsUserEntity = newsUserRepository.findByEmail(email)
                .orElseThrow(NewsUserNotFoundException::new);

        return entityToDto(newsUserEntity);
    }

    @Override
    public NewsUserDto findByEmailAndPassword(String email, String password) throws NewsUserNotFoundException {
        NewsUserEntity newsUserEntity = newsUserRepository.findByEmailAndPassword(email, password)
                .orElseThrow(NewsUserNotFoundException::new);

        return entityToDto(newsUserEntity);
    }

    @Override
    public void save(NewsUserDto newsUserDto) {
        newsUserRepository.save(dtoToEntity(newsUserDto));
    }

    @Override
    public Set<Long> findAllBySource(String source) {
        List<NewsUserEntity> storedUsers = newsUserRepository.findBySubscription_Source(source);
        return storedUsers.stream().map(user -> user.getId())
                .collect(Collectors.toSet());
    }

    private NewsUserEntity dtoToEntity(NewsUserDto dto) {
        NewsUserEntity entity = new NewsUserEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setRegistrationSource(dto.getRegistrationSource());
        return entity;
    }

    private static NewsUserDto entityToDto(NewsUserEntity newsUserEntity) {
        NewsUserDto newsUserDto = new NewsUserDto();
        newsUserDto.setId(newsUserEntity.getId());
        newsUserDto.setName(newsUserEntity.getName());
        newsUserDto.setEmail(newsUserEntity.getEmail());
        newsUserDto.setRole(newsUserEntity.getRole());
        newsUserDto.setRegistrationSource(newsUserEntity.getRegistrationSource());
        return newsUserDto;
    }
}
