package com.ecommerce.booksale.cart;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDTO {
    private Integer bookId;
    private String bookTitle;
    private String imgUrl;
    private Double price;
    private String discountPrice;
    private Integer quantity;
    private Integer availableQuantity;
    private Boolean status;


    public Boolean getStatus(){
        return this.quantity < this.availableQuantity;
    }
}
