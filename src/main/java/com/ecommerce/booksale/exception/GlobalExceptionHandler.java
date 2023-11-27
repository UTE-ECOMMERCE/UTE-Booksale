package com.ecommerce.booksale.exception;


import com.ecommerce.booksale.authentication.registration.RegisterData;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        return new ModelAndView("register")
                .addObject("error", bindingResult.getFieldError().getDefaultMessage())
                .addObject("registerData", new RegisterData()); // Add an empty RegisterData object to the model
    }
    @ExceptionHandler(BookNotFoundByCategory.class)
    public ModelAndView handleBooksNotFoundOrEmpty(RuntimeException ex){
        return new ModelAndView("empty-book-category");
    }

    @ExceptionHandler(BadRequestException.class)
    public ModelAndView handleBadRequestException(BadRequestException ex){
        return new ModelAndView("redirect:/cart")
                .addObject("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception ex){
        System.out.println(ex.getLocalizedMessage());
        return new ModelAndView("500");
    }
}
