package com.ecommerce.booksale.cart;

import com.ecommerce.booksale.book.Book;
import com.ecommerce.booksale.book.BookDTO;
import com.ecommerce.booksale.book.BookMapper;
import com.ecommerce.booksale.book.BookRepository;
import com.ecommerce.booksale.exception.BookNotFoundException;
import com.ecommerce.booksale.exception.Messages;
import com.ecommerce.booksale.exception.UserNotFoundException;
import com.ecommerce.booksale.order.Order;
import com.ecommerce.booksale.order.orderItem.OrderItem;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    public List<OrderItem> getOrderedItems(ShoppingCart cart, Order order) {
        List<OrderItem> orderedItems = new ArrayList<>();
        List<CartDTO> chosenItems = cart.getItems().stream()
                .filter(CartDTO::getChosen)
                .collect(Collectors.toList());

        chosenItems.forEach(item -> {
            Book foundBook = bookRepository.findById(item.getBookId()).orElse(null);
            if (foundBook != null) {
                foundBook.setQuantity(foundBook.getQuantity() - item.getQuantity());
                bookRepository.save(foundBook);
            }

            OrderItem orderItem = OrderItem.builder()
                    .quantity(item.getQuantity())
                    .book(foundBook)
                    .totalPrice(item.getQuantity() * item.getPrice())
                    .order(order)
                    .build();
            orderedItems.add(orderItem);
        });

        chosenItems.forEach(cartItem -> cart.deleteItem(cartItem.getBookId())); // Assuming getBookId returns int ID

        return orderedItems;
    }

}
