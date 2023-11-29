package com.ecommerce.booksale.cart;

import com.ecommerce.booksale.book.Book;
import com.ecommerce.booksale.book.BookDTO;
import com.ecommerce.booksale.book.BookMapper;
import com.ecommerce.booksale.book.BookRepository;
import com.ecommerce.booksale.exception.BookNotFoundException;
import com.ecommerce.booksale.exception.Messages;
import com.ecommerce.booksale.exception.UserNotFoundException;
import com.ecommerce.booksale.user.User;
import com.ecommerce.booksale.user.UserRepository;
import com.ecommerce.booksale.user.address.Address;
import com.ecommerce.booksale.user.address.AddressDTO;
import com.ecommerce.booksale.user.address.AddressMapper;
import com.ecommerce.booksale.utils.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@AllArgsConstructor
public class CartService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
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
                .availableQuantity(foundBook.getQuantity())
                .chosen(false)
                .build();

        return cartBook;
    }

    public ServiceResponse renderAddress(String phoneNumer){
       User existingUser = userRepository.findByPhone(phoneNumer).orElse(null);

       if (existingUser == null){
           return ServiceResponse.builder()
                   .code(HttpStatus.BAD_REQUEST.value())
                   .message(Messages.USER_NOT_FOUND)
                   .build();
       }

       Address userAddress = existingUser.getAddresses().get(0);

       AddressDTO result = addressMapper.addressToAddressDTO(userAddress);


       return ServiceResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Get user information successfully !")
                .data(Map.of("address", result))
                .build();
    }

}
