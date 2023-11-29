package com.ecommerce.booksale.order;


import com.ecommerce.booksale.book.BookRepository;
import com.ecommerce.booksale.cart.ShoppingCart;
import com.ecommerce.booksale.user.address.AddressDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
@SessionAttributes("cart")
public class OrderController {

    private final OrderService orderService;

    @ModelAttribute("cart")
    public ShoppingCart createCart(){
        return new ShoppingCart();
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("userAddress") @Valid AddressDTO userAddress,
                              BindingResult result,
                              @ModelAttribute("cart") ShoppingCart cart,
                               Model model){
        System.out.println("Inside this function");


        log.info("User address: {}", userAddress.getUserInformation().getEmail());

        String errorMessage;
        String message = "Đặt hàng thành công";
        if (result.hasErrors()) {
            // There are validation errors, handle them
            // Get the first field error message
            log.info("Error: {}", result.getFieldErrors().get(0).toString());
            errorMessage = result.getFieldErrors().get(0).toString();
            // Add the first error message to the model
            System.out.println(errorMessage);
            model.addAttribute("showModal", true);
            model.addAttribute("errorMessage", errorMessage);
            return "cart";
        }


        boolean isCreated = orderService.createOrder(userAddress, cart);
        System.out.println("isCreated: " + isCreated);
        if (!isCreated) {
            message = "Đặt hàng thất bại";
            model.addAttribute("showModal", true);
            model.addAttribute("message", message );
            return "redirect:/cart";
        }

        model.addAttribute("showModal", true);
        model.addAttribute("message", message );
        return "redirect:/cart";
    }
}
