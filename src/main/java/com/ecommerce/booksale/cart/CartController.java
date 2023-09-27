package com.ecommerce.booksale.cart;


import com.ecommerce.booksale.book.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping()
@AllArgsConstructor
@SessionAttributes("cart")
public class CartController {

    private final BookService bookService;
    private final CartService cartService;

    @ModelAttribute("cart")
    public ShoppingCart createCart(){
        return new ShoppingCart();
    }

    @GetMapping("/cart")
    public String handleGetCart(@ModelAttribute("cart") ShoppingCart cart){
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("bookId") int id,
                            @RequestParam("quantity") int quantity,
                            @ModelAttribute("cart") ShoppingCart cart){

        CartDTO newCartItem = cartService.getBookCartItem(id, quantity);
        cart.addBook(newCartItem);
        return "redirect:cart";
    }
}
