package com.ecommerce.booksale.cart;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestData {
    private Integer id;
    private Integer quantity;
    private Boolean chosen;
}
