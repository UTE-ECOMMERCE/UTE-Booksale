package com.ecommerce.booksale.home;

import com.ecommerce.booksale.book.Book;
import com.ecommerce.booksale.book.BookService;
import com.ecommerce.booksale.cart.ShoppingCart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
@SessionAttributes("cart")
public class HomeController {

    private final BookService bookService;

    @GetMapping("/")
    public String renderHomePage( Model theModel ) {

        // get all books from service
        List<Book> books = bookService.findTopBooks();


        // add attributes to model
        bookService.getHomeBookData(theModel);
        theModel.addAttribute("books", books);

        return "index";
    }
}
