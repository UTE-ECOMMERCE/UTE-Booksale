package com.ecommerce.booksale.order;


import com.ecommerce.booksale.cart.CartService;
import com.ecommerce.booksale.cart.ShoppingCart;
import com.ecommerce.booksale.order.orderItem.OrderItem;
import com.ecommerce.booksale.user.User;
import com.ecommerce.booksale.user.UserRepository;
import com.ecommerce.booksale.user.address.Address;
import com.ecommerce.booksale.user.address.AddressDTO;
import com.ecommerce.booksale.utils.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final EmailService mailSender;

    private String orderHtmlTemplate(Order order) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>\n");
        htmlContent.append("<html>\n");
        htmlContent.append("<head>\n");
        htmlContent.append("    <title>Order Confirmation</title>\n");
        htmlContent.append("    <style>\n");
        htmlContent.append("        body {\n");
        htmlContent.append("            font-family: Arial, sans-serif;\n");
        htmlContent.append("            margin: 0;\n");
        htmlContent.append("            padding: 0;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .container {\n");
        htmlContent.append("            width: 80%;\n");
        htmlContent.append("            margin: auto;\n");
        htmlContent.append("            padding: 20px;\n");
        htmlContent.append("            border: 1px solid #ccc;\n");
        htmlContent.append("            border-radius: 5px;\n");
        htmlContent.append("            background-color: #f9f9f9;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .header {\n");
        htmlContent.append("            text-align: center;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .order-details {\n");
        htmlContent.append("            margin-top: 20px;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .order-info {\n");
        htmlContent.append("            border-collapse: collapse;\n");
        htmlContent.append("            width: 100%;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .order-info th, .order-info td {\n");
        htmlContent.append("            border: 1px solid #ddd;\n");
        htmlContent.append("            padding: 8px;\n");
        htmlContent.append("            text-align: left;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .order-info th {\n");
        htmlContent.append("            background-color: #f2f2f2;\n");
        htmlContent.append("        }\n");
        htmlContent.append("    </style>\n");
        htmlContent.append("</head>\n");
        htmlContent.append("<body>\n");
        htmlContent.append("    <div class=\"container\">\n");
        htmlContent.append("        <div class=\"header\">\n");
        htmlContent.append("            <h2>Order Confirmation</h2>\n");
        htmlContent.append("        </div>\n");
        htmlContent.append("        <div class=\"order-details\">\n");
        htmlContent.append("            <p>Dear ").append(order.getUser().getFullName()).append(",</p>\n");
        htmlContent.append("            <p>Your order has been successfully placed. Below are the details:</p>\n");
        htmlContent.append("            <table class=\"order-info\">\n");
        htmlContent.append("                <thead>\n");
        htmlContent.append("                    <tr>\n");
        htmlContent.append("                        <th>Product</th>\n");
        htmlContent.append("                        <th>Quantity</th>\n");
        htmlContent.append("                        <th>Price</th>\n");
        htmlContent.append("                    </tr>\n");
        htmlContent.append("                </thead>\n");
        htmlContent.append("                <tbody>\n");

        for (OrderItem item : order.getOrderItems()) {
            htmlContent.append("                    <tr>\n");
            htmlContent.append("                        <td>").append(item.getBook().getTitle()).append("</td>\n");
            htmlContent.append("                        <td>").append(item.getQuantity()).append("</td>\n");
            htmlContent.append("                        <td>").append(item.getTotalPrice()).append("</td>\n");
            htmlContent.append("                    </tr>\n");
        }

        htmlContent.append("                </tbody>\n");
        htmlContent.append("            </table>\n");
        htmlContent.append("            <p>Total amount: $").append(order.getTotalPrice()).append("</p>\n");
        htmlContent.append("            <p>Thank you for shopping with us!</p>\n");
        htmlContent.append("        </div>\n");
        htmlContent.append("    </div>\n");
        htmlContent.append("</body>\n");
        htmlContent.append("</html>");

        return htmlContent.toString();
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


                // Tạo CompletableFuture cho việc gửi email bất đồng bộ
                CompletableFuture.runAsync(() -> {
                    mailSender.send(
                            newUser.getEmail(),
                            "Order Confirmation",
                            orderHtmlTemplate(newOrder));
                });

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

                CompletableFuture.runAsync(() -> {
                    mailSender.send(
                            foundUser.getEmail(),
                            "Order Confirmation",
                            orderHtmlTemplate(newOrder));
                });

                log.info("Order placed for existing user: {}", foundUser.getEmail());
            }

            return true;
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            return false;
        }
    }

}
