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
import org.springframework.validation.FieldError;
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
    public String createOrder(@Valid @ModelAttribute("userAddress") AddressDTO userAddress,
                              BindingResult result,
                              @ModelAttribute("cart") ShoppingCart cart,
                               Model model){

        String errorMessage;
        String message = "Đặt hàng thành công";
        if (result.hasErrors()) {
            // There are validation errors, handle them
            errorMessage = "Thiếu thông tin. " + result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .findFirst()
                    .orElse("Lỗi không xác định");
            model.addAttribute("showModal", true);
            model.addAttribute("message", errorMessage);
            return "cart";
        }

        if (!cart.checkCreateOrderValid()){
            message = "Đặt hàng thất bại. Bạn chưa chọn sản phẩm nào";
            System.out.println(message);
            model.addAttribute("showModal", true);
            model.addAttribute("message", message );
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
