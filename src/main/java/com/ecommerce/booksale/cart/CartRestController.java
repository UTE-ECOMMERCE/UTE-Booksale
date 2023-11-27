package com.ecommerce.booksale.cart;


import com.ecommerce.booksale.utils.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    @GetMapping("/address")
    public ResponseEntity fillAddress(@RequestParam("phone") String phone){
        ServiceResponse serviceResponse = cartService.renderAddress(phone);

        return ResponseEntity
                .status(serviceResponse.getCode())
                .body(serviceResponse);
    }
}
