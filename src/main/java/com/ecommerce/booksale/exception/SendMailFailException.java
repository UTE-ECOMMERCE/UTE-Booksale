package com.ecommerce.booksale.exception;

public class SendMailFailException extends RuntimeException{
    public SendMailFailException(String message) {
        super(message);
    }
}
