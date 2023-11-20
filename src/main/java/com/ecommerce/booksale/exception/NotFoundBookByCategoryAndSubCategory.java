package com.ecommerce.booksale.exception;


public class NotFoundBookByCategoryAndSubCategory extends RuntimeException {
    public NotFoundBookByCategoryAndSubCategory(String message){
        super(message);
    }
}
