package com.ecommerce.booksale.exception;

public class RelevantBookNotFoundException extends RuntimeException{
    public RelevantBookNotFoundException(String message) {
        super(message);
    }
}
