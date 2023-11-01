package com.ecommerce.booksale.cart;


import com.ecommerce.booksale.book.BookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {
    private final CartService cartService;
    @ModelAttribute("cart")
    public ShoppingCart createCart(){
        return new ShoppingCart();
    }
    @GetMapping()
    public String handleGetCart(Model model, HttpServletRequest request){
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("bookId") int id,
                            @RequestParam("quantity") int quantity,
                            @ModelAttribute("cart") ShoppingCart cart){
        CartDTO newCartItem = cartService.getBookCartItem(id, quantity);
        cart.addBook(newCartItem);
        return "redirect:/cart";
    }

    @PostMapping("/update-quantity")
    public String updateQuantityItem(@RequestBody CartRequestData requestData,
                                     @ModelAttribute("cart") ShoppingCart cart){
        int bookId = requestData.getId();
        int updatedQuantity = requestData.getQuantity();
        cart.updateQuantityItem(bookId, updatedQuantity);
        return "cart";
    }

    @PostMapping("/delete")
    public String deleteCartItem(@RequestBody CartRequestData  requestData,
                                 @ModelAttribute("cart") ShoppingCart cart){
        int bookId = requestData.getId();
        cart.deleteItem(bookId);
        return "cart";
    }
}
