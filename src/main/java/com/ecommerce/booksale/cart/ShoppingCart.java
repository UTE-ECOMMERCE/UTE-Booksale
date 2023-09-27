package com.ecommerce.booksale.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    private List<CartDTO> items = new ArrayList<>();



    public void addBook(CartDTO newItem) {
        boolean isDuplicate = items.stream()
                .anyMatch(item -> newItem.getBookId() == item.getBookId());

        if (isDuplicate) {
            items.stream()
                    .filter(item -> newItem.getBookId() == item.getBookId())
                    .forEach(item -> item.setQuantity(item.getQuantity() + newItem.getQuantity()));
        } else {
            items.add(newItem);
        }
    }


}
