package com.ecommerce.booksale.cart;

import com.ecommerce.booksale.book.Book;
import com.ecommerce.booksale.book.BookDTO;
import com.ecommerce.booksale.book.BookMapper;
import com.ecommerce.booksale.book.BookRepository;
import com.ecommerce.booksale.exception.BookNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CartService {

    private BookRepository bookRepository;

    public CartDTO getBookCartItem (int id, int quantity){
        // found book with the given id
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Không tìm thấy sách với id " + id));

        // convert to book DTO
        BookDTO bookData = BookMapper.toDTO(foundBook);

        // create new CartDTO item
        CartDTO cartBook = CartDTO.builder()
                .bookId(id)
                .quantity(quantity)
                .bookTitle(bookData.getTitle())
                .imgUrl(bookData.getMainImg())
                .price(bookData.getPrice())
                .discountPrice(bookData.getFinalPrice())
                .build();

        return cartBook;
    }


}
