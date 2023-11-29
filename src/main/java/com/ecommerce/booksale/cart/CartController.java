package com.ecommerce.booksale.cart;


import com.ecommerce.booksale.book.Book;
import com.ecommerce.booksale.book.BookRepository;
import com.ecommerce.booksale.book.BookService;
import com.ecommerce.booksale.exception.BookNotFoundException;
import com.ecommerce.booksale.exception.Messages;
import com.ecommerce.booksale.user.address.Address;
import com.ecommerce.booksale.user.address.AddressDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cart")
@SessionAttributes("cart")
@Slf4j
public class CartController {
    private final CartService cartService;
    private final BookRepository bookRepository;
    @ModelAttribute("cart")
    public ShoppingCart createCart(){
        return new ShoppingCart();
    }
    @GetMapping()
    public String handleGetCart(Model model){
        AddressDTO userAddress = AddressDTO.builder().build();

        // Use userAddress as a model attribute for your view
        model.addAttribute("userAddress", userAddress);
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestBody CartRequestData requestData,
                            @ModelAttribute("cart") ShoppingCart cart){
        CartDTO newCartItem = cartService.getBookCartItem(requestData.getId(), requestData.getQuantity());

        cart.addBook(newCartItem);
        return "redirect:/cart";
    }

    @PostMapping("/update-quantity")
    public String updateQuantityItem(@RequestBody CartRequestData requestData,
                                     @ModelAttribute("cart") ShoppingCart cart){
        int bookId = requestData.getId();
        int updatedQuantity = requestData.getQuantity();
        cart.updateQuantityItem(bookId, updatedQuantity);
        return "redirect:/cart";
    }

    @PostMapping("/delete")
    public String deleteCartItem(@RequestBody CartRequestData  requestData,
                                 @ModelAttribute("cart") ShoppingCart cart){
        int bookId = requestData.getId();
        cart.deleteItem(bookId);
        return "redirect:/cart";
    }

    @PostMapping("/choose-item")
    public String chooseItemUpdate(@RequestBody CartRequestData  requestData,
            @ModelAttribute("cart") ShoppingCart cart){

        cart.updateStatusItem(requestData.getId(), requestData.getChosen());
        return "redirect:/cart";
    }
}
