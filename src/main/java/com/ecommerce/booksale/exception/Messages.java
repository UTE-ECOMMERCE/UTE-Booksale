package com.ecommerce.booksale.exception;

public interface Messages {
    public static final String CATEGORY_NOT_FOUND = "Category %s not found";
    public static final String SUBCATEGORY_NOT_FOUND = "Subcategory %s not found";
    public static final String BOOK_LIST_EMPTY = "Book list is empty";
    public static final String BOOK_NOT_FOUND = "Book %s not found";
    public static final String BOOK_NOT_FOUND_BY_CATEGORY = "Book not found by category %s";
    public static  final String BOOK_NOT_FOUND_BY_CATEGORY_AND_SUBCATEGORY = "Book not found by category %s and subcategory %s";
    public static final String SEND_EMAIL_ERROR = "Can not send email";

    public static final String USER_NOT_FOUND = "User with email %s not found";
    String USER_NOT_FOUND_BY_PHONE_NUMBER = "User not found by phone number %p";
}
