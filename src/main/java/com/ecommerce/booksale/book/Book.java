package com.ecommerce.booksale.book;


import com.ecommerce.booksale.book.author.Author;
import com.ecommerce.booksale.book.category.Category;
import com.ecommerce.booksale.book.image.Image;
import com.ecommerce.booksale.book.publisher.Publisher;
import com.ecommerce.booksale.book.subcategory.SubCategory;
import com.ecommerce.booksale.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Integer bookId;

    @Column(name="title")
    private String title;

    @Column(name="publication_year")
    private Integer publicationYear;

    @Column(name="description")
    private String bookDescription;

    @Column(name="discount")
    private Integer discount;

    @Column(name="total_pages",nullable = true)
    private Integer totalPages;

    @Column(name="main_img")
    private String mainImg;

    @Column(name="price")
    private double price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="cover_type")
    private String coverType;

    @Column(name="is_active")
    private boolean isActive;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="author_id")
    @JsonIgnore
    private Author author;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="publisher_id")
    @JsonIgnore
    private Publisher publisher;

    // One book has many categories and one category can be in many books
    @ManyToMany(fetch = FetchType.LAZY, cascade =
            {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="Book_Category",
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"))
    @JsonIgnore
    private List<Category> categories;

    // One book can have many subcategories and one subCategory can be in different books
    @ManyToMany(fetch = FetchType.LAZY, cascade =
            {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="Book_Subcategory",
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="subcategory_id"))
    private List<SubCategory> subcategories;

    // One book can have many others image
    @OneToMany(mappedBy = "book" , cascade =
                    {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Image> images;

    // One book can be in many orders and one order can have many book
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="Order_Items",
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="order_id"))
    private List<Order> orders;

}
