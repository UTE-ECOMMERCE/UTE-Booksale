package com.ecommerce.booksale.order;


import com.ecommerce.booksale.cart.CartService;
import com.ecommerce.booksale.cart.ShoppingCart;
import com.ecommerce.booksale.order.orderItem.OrderItem;
import com.ecommerce.booksale.user.User;
import com.ecommerce.booksale.user.UserRepository;
import com.ecommerce.booksale.user.address.Address;
import com.ecommerce.booksale.user.address.AddressDTO;
import com.ecommerce.booksale.user.address.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final UserRepository userRepository;
    private final CartService cartService;

    private String orderHtmlTemplate(Order order) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Order Confirmation</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 80%;\n" +
                "            margin: auto;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 5px;\n" +
                "            background-color: #f9f9f9;\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .order-details {\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .order-info {\n" +
                "            border-collapse: collapse;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        .order-info th, .order-info td {\n" +
                "            border: 1px solid #ddd;\n" +
                "            padding: 8px;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .order-info th {\n" +
                "            background-color: #f2f2f2;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h2>Order Confirmation</h2>\n" +
                "        </div>\n" +
                "        <div class=\"order-details\">\n" +
                "            <p>Dear [Customer Name],</p>\n" +
                "            <p>Your order has been successfully placed. Below are the details:</p>\n" +
                "            <table class=\"order-info\">\n" +
                "                <thead>\n" +
                "                    <tr>\n" +
                "                        <th>Product</th>\n" +
                "                        <th>Quantity</th>\n" +
                "                        <th>Price</th>\n" +
                "                    </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                    <!-- Iterate through order items -->\n" +
                "                    {{#each orderItems}}\n" +
                "                    <tr>\n" +
                "                        <td>{{this.productName}}</td>\n" +
                "                        <td>{{this.quantity}}</td>\n" +
                "                        <td>{{this.price}}</td>\n" +
                "                    </tr>\n" +
                "                    {{/each}}\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "            <p>Total amount: ${{totalAmount}}</p>\n" +
                "            <p>Thank you for shopping with us!</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }



    @Transactional // Ensure transactional behavior
    public boolean createOrder(AddressDTO userAddress, ShoppingCart cart) {
        try {
            User foundUser = userRepository.findByPhone(userAddress.getUserInformation().getPhone()).orElse(null);

            if (foundUser == null) {
                User newUser = User.builder()
                        .fullName(userAddress.getUserInformation().getFullName())
                        .email(userAddress.getUserInformation().getEmail())
                        .phone(userAddress.getUserInformation().getPhone()) // Fix: phone should be set using phone, not email
                        .build();

                Address newAddress = Address.builder()
                        .province(userAddress.getProvince())
                        .district(userAddress.getDistrict())
                        .ward(userAddress.getWard())
                        .street(userAddress.getStreet())
                        .description(userAddress.getDescription())
                        .build();

                Order newOrder = Order.builder()
                        .orderDate(new Date())
                        .totalPrice(cart.getTotalPrice())
                        .build();

                List<OrderItem> orderedItems = cartService.getOrderedItems(cart, newOrder);
                newOrder.setOrderItems(orderedItems);

                newUser.addOrders(newOrder);
                newUser.addAddress(newAddress);
                userRepository.save(newUser);

                log.info("New user created: {}", newUser.getEmail());
            } else {
                Order newOrder = Order.builder()
                        .orderDate(new Date())
                        .totalPrice(cart.getTotalPrice())
                        .user(foundUser)
                        .build();

                List<OrderItem> orderedItems = cartService.getOrderedItems(cart, newOrder);
                newOrder.setOrderItems(orderedItems);

                foundUser.addOrders(newOrder);
                userRepository.save(foundUser);

                log.info("Order placed for existing user: {}", foundUser.getEmail());
            }

            return true;
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            return false;
        }
    }

}
