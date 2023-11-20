package com.ecommerce.booksale.exception;

public class BookNotFoundByCategory extends RuntimeException{
    public BookNotFoundByCategory(String message){
        super(message);
    }
}
