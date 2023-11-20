package com.ecommerce.booksale.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public void updateQuantityItem(Integer id, Integer quantity){
        items.stream().filter(item -> item.getBookId() == id)
                .forEach(item -> item.setQuantity(quantity));
    }

    public boolean deleteItem(int id){
        Iterator<CartDTO> itr = items.iterator();

        while (itr.hasNext()){
            CartDTO item = itr.next();

            if (item.getBookId() == id){
                itr.remove();
                return true;
            }
        }
        return false;
    }


}
