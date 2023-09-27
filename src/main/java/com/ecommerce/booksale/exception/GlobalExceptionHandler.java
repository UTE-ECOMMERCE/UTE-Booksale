package com.ecommerce.booksale.exception;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception ex){
        System.out.println(ex.getLocalizedMessage());
        return new ModelAndView("500");
    }
}
