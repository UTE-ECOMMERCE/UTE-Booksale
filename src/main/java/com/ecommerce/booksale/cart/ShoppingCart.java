package com.ecommerce.booksale.cart;

import com.ecommerce.booksale.book.Book;
import com.ecommerce.booksale.book.BookRepository;
import com.ecommerce.booksale.exception.BadRequestException;
import com.ecommerce.booksale.exception.BookNotFoundException;
import com.ecommerce.booksale.exception.Messages;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
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

    public void updateQuantityItem(Integer bookId, Integer quantity){
        items.stream().filter(item -> item.getBookId() == bookId)
                .forEach(item ->
                    item.setQuantity(quantity));

        System.out.println(items.get(0).getQuantity() + " " + items.get(0).getAvailableQuantity());
        System.out.println(items.get(0).getStatus());
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
