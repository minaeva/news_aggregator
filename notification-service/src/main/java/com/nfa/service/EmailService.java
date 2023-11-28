package com.nfa.service;

import java.util.Set;

public interface EmailService {

    void sendEmailAsync(Set<String> emails);

    void sendEmail(String email);
}
