package com.ecommerce.booksale.order;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
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
}
