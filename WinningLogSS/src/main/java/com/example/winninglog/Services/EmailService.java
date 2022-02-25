package com.example.winninglog.Services;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface EmailService {
    void sendRetrievalMail(String retrieval, String mailTo) throws MessagingException;
}
