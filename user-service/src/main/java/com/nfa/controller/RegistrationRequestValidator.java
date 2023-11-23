package com.nfa.controller;

import com.nfa.controller.request.RegistrationRequest;
import com.nfa.entity.RegistrationSource;
import com.nfa.exception.ReaderValidationException;
import lombok.extern.java.Log;
import org.springframework.util.StringUtils;

@Log
public class RegistrationRequestValidator {

    private RegistrationRequestValidator() {
    }

    public static void validateRegistrationRequest(RegistrationRequest request) {

        if (RegistrationSource.ONSITE.equals(request.getRegistrationSource()) &&
                !StringUtils.hasLength(request.getPassword())) {
            throw new ReaderValidationException("Password cannot be empty");
        }

        if (!StringUtils.hasLength(request.getEmail())) {
            throw new ReaderValidationException("Email cannot be empty");
        }

        if (request.getRegistrationSource() == null) {
            throw new ReaderValidationException("Registration source cannot be empty");
        }

    }

//    public static ReaderDto validateGoogleIdToken(String token) throws GeneralSecurityException, IOException,
//            BoobookValidationException {
//
//        GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
//                .Builder(new ApacheHttpTransport(), gsonFactory)
//                .setAudience(Collections.singletonList(CLIENT_ID))
//                .build();
//
//        GoogleIdToken idToken = verifier.verify(token);
//
//        if (idToken == null) {
//            throw new BoobookValidationException("Invalid ID token.");
//        }
//
//        Payload payload = idToken.getPayload();
//
//        // not sure if needed
//        String userId = payload.getSubject();
//        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//        if (!emailVerified) {
//            log.warning("Email " + payload.getEmail() + " is not verified.");
//        }
//
//        ReaderDto readerDto = new ReaderDto();
//        readerDto.setEmail(payload.getEmail());
//        readerDto.setRegistrationType(RegistrationType.GOOGLE);
//        readerDto.setName((String) payload.get("given_name"));
//        readerDto.setSurname((String) payload.get("family_name"));
//        return readerDto;
//    }
}


