package com.nfa.service;

import com.nfa.dto.HashedPasswordAndSaltDto;
import com.nfa.dto.ReaderDto;
import com.nfa.entity.Reader;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.exception.ReaderUnauthorizedException;
import com.nfa.exception.ReaderValidationException;
import com.nfa.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    @Override
    public ReaderDto findByEmail(String email) {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(() -> new ReaderNotFoundException(
                        String.format("No reader with email %s found", email)));

        log.info("foundByEmail user ", reader);
        return toDto(reader);
    }

    @Override
    public ReaderDto authenticate(String email, String password) {
        Reader savedReader = readerRepository.findByEmail(email)
                .orElseThrow(() -> new ReaderNotFoundException(
                        String.format("No reader with email %s found", email)));
        String savedReaderSalt = savedReader.getSalt();
        String hashedPassword = getHashed(password + savedReaderSalt);
        String savedReaderPassword = savedReader.getPassword();

        if (hashedPassword.equals(savedReaderPassword)) {
            return toDto(savedReader);

        }
        throw new ReaderUnauthorizedException("Password is wrong");
    }

    @Override
    public void save(ReaderDto readerDto) {
        try {
            readerRepository.save(toEntity(readerDto, hashPasswordAndSalt(readerDto.password())));
        } catch (Exception e) {
            throw new ReaderValidationException(e.getMessage());
        }
    }

    private HashedPasswordAndSaltDto hashPasswordAndSalt(String password) {
        SecureRandom random = new SecureRandom();
        byte[] saltArray = new byte[16];
        random.nextBytes(saltArray);
        String salt = Base64.getEncoder().encodeToString(saltArray);
        String encodedPasswordAndSalt = getHashed(password + salt);
        return new HashedPasswordAndSaltDto(encodedPasswordAndSalt, salt);
    }

    private static String getHashed(String stringToHash) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password ", e);
        }

        byte[] hashedPasswordAndEncodedSalt = md.digest(stringToHash.getBytes());
        return Base64.getEncoder().encodeToString(hashedPasswordAndEncodedSalt);
    }

    private Reader toEntity(ReaderDto dto, HashedPasswordAndSaltDto hashedPasswordAndSalt) {
        Reader entity = new Reader();
        entity.setName(dto.name());
        entity.setPassword(hashedPasswordAndSalt.password());
        entity.setSalt(hashedPasswordAndSalt.salt());
        entity.setEmail(dto.email());
        entity.setRole(dto.role());
        entity.setRegistrationSource(dto.registrationSource());
        return entity;
    }

    public static ReaderDto toDto(Reader reader) {
        return new ReaderDto(reader.getId(), reader.getName(), null,
                reader.getEmail(), reader.getRole(), reader.getRegistrationSource());
    }
}
