package com.ecommerce.booksale.utils.email;

public interface EmailSender {
    void send(String to, String subject, String email);
}
