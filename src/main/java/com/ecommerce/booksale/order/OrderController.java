package com.ecommerce.booksale.order;


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

        String errorMessage;
        if (result.hasErrors()) {
            // There are validation errors, handle them
            // Get the first field error message
            errorMessage = result.getFieldErrors().get(0).toString();
            // Add the first error message to the model
            model.addAttribute("showModal", true);
            model.addAttribute("errorMessage", errorMessage);
            return "cart";
        }

        if (cart.checkCreateOrderValid()){
            errorMessage= "Bạn chưa chọn mua sản phẩm nào";
            model.addAttribute("showModal", true);
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/cart";
        }

        log.info("User address: {}", userAddress.getUserInformation().getEmail());
        return "cart";
    }
}
