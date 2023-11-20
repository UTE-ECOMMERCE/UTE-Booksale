package com.ecommerce.booksale.exception;

public class SubCategoryNotFoundException extends RuntimeException {
    public SubCategoryNotFoundException(String message){
        super(message);
    }
}
