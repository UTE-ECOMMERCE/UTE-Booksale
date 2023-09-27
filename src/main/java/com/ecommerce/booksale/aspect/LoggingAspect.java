package com.ecommerce.booksale.aspect;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {

//    @Before("execution(* com.ecommerce.booksale.book.*.*(..))")
//    public void bookService (){
//        System.out.println(getClass() + " @Advice before method");
//    }
}
