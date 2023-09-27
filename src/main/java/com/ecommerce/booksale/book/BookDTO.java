package com.ecommerce.booksale.book;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class BookDTO {
    private int bookId;
    private String title;
    private int publicationYear;
    private int discount;
    private String mainImg;
    private double price;
    private int pages;
    private String desc;
    private String coverType;
    private int quantity;
    private boolean isActive;
    private String finalPrice;
    private String authorName;
    private int publisherId;
    private List<Integer> categoryIds;
    private List<Integer> subcategoryIds;
    private List<Integer> imageIds;
}
