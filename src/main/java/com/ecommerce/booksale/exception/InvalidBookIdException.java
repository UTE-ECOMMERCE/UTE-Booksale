package com.ecommerce.booksale.exception;

public class InvalidBookIdException extends NumberFormatException{
    public InvalidBookIdException() {
        super();
    }

    public InvalidBookIdException(String s) {
        super(s);
    }
}
